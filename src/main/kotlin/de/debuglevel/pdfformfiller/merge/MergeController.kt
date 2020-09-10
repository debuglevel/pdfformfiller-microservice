package de.debuglevel.pdfformfiller.merge

import de.debuglevel.pdfformfiller.form.FormService
import de.debuglevel.pdfmergefiller.merge.MergeService
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import mu.KotlinLogging
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.*

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/merges")
class MergeController(
    private val merger: OpenpdfMerger,
    private val formService: FormService,
    private val mergeService: MergeService,
) {
    private val logger = KotlinLogging.logger {}

    /**
     * @implNote This is implemented in a blocking way, because the merge operation is usually really fast (~10ms), and
     *           an asynchronous design would just add overhead without real benefit. When there are really big PDFs or
     *           many PDFs, an asynchronous design with a worker queue might be reasonable.
     */
    @Post("/")
    fun postOne(addMergeRequest: AddMergeRequest): HttpResponse<*> {
        logger.debug("Called postOne($addMergeRequest)")
        return try {
            val pdf = getPdf(addMergeRequest)
            val resultPdf = ByteArrayOutputStream()
            merger.merge(pdf, addMergeRequest.values, resultPdf)

            val merge = Merge(
                id = null,
                pdf = resultPdf.toByteArray()
            )

            val savedMerge = mergeService.add(merge)

            val mergeResponse = AddMergeResponse(savedMerge)
            HttpResponse.created(mergeResponse)
        } catch (e: FormService.InvalidPdfException) {
            HttpResponse.badRequest("The given PDF is invalid.")
        } catch (e: FormService.FormNotFoundException) {
            HttpResponse.badRequest("Form ${addMergeRequest.pdfId} not found.")
        } catch (e: Exception) {
            logger.error(e) { "Unhandled exception" }
            HttpResponse.serverError("Unhandled exception: " + e.message)
        }
    }

    private fun getPdf(addMergeRequest: AddMergeRequest): ByteArrayInputStream {
        return if (addMergeRequest.pdfId != null) {
            logger.debug { "Using existing form with ID '${addMergeRequest.pdfId}'..." }
            formService.get(addMergeRequest.pdfId).pdf.inputStream()
        } else if (!addMergeRequest.pdf.isNullOrBlank()) {
            logger.debug { "Using form embedded in request..." }
            Base64.getDecoder().decode(addMergeRequest.pdf).inputStream()
        } else {
            throw MissingPdfException()
        }
    }

    @Get("/{id}")
    @Produces("application/pdf")
    fun getOne(id: UUID): HttpResponse<*> {
        logger.debug("Called getOne($id)")
        return try {
            val getMerge = mergeService.get(id)

            HttpResponse.ok(getMerge.pdf)
        } catch (e: MergeService.MergeNotFoundException) {
            HttpResponse.notFound("Merge $id not found.")
        } catch (e: Exception) {
            logger.error(e) { "Unhandled exception" }
            HttpResponse.serverError("Unhandled exception: " + e.message)
        }
    }

    @Delete("/{id}")
    fun deleteOne(id: UUID): HttpResponse<*> {
        logger.debug("Called deleteOne($id)")
        return try {
            mergeService.delete(id)

            HttpResponse.noContent<Any>()
        } catch (e: MergeService.MergeNotFoundException) {
            HttpResponse.notFound("Merge $id not found.")
        } catch (e: Exception) {
            logger.error(e) { "Unhandled exception" }
            HttpResponse.serverError("Unhandled exception: " + e.message)
        }
    }

    class MissingPdfException : Exception("Neither 'pdf' nor 'pdfId' was given.")
}