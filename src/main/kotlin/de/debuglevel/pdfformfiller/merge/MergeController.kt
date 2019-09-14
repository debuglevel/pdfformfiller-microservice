package de.debuglevel.pdfformfiller.merge

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import mu.KotlinLogging
import java.io.ByteArrayOutputStream
import java.util.*

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/merges")
class MergeController(private val merger: OpenpdfMerger) {
    private val logger = KotlinLogging.logger {}

    // TODO: For now, this is a simple map; i.e. an in-memory database
    //       For persistence and lower memory consumption, data should be stored in a persistent database
    private val resultPdfStorage = mutableMapOf<UUID, ByteArray>()

    /**
     * @implNote This is implemented in a blocking way, because the merge operation is usually really fast (~10ms), and
     *           an asynchronous design would just add overhead without real benefit. When there are really big PDFs or
     *           many PDFs, an asynchronous design with a worker queue might be reasonable.
     */
    @Post("/")
    fun postOne(mergeRequest: MergeRequest): HttpResponse<MergeResponse> {
        logger.debug("Called postOne($mergeRequest)")

        return run {
            val pdf = Base64.getDecoder().decode(mergeRequest.pdf).inputStream()
            val data = convertFieldsToMap(mergeRequest.data)
            val resultPdf = ByteArrayOutputStream()
            merger.merge(pdf, data, resultPdf)

            val uuid = UUID.randomUUID()
            resultPdfStorage[uuid] = resultPdf.toByteArray()

            val mergeResponse = MergeResponse(uuid)
            HttpResponse.created(mergeResponse)
        }
    }

    @Get("/{uuid}")
    fun getOne(uuid: String): HttpResponse<ByteArray> {
        logger.debug("Called getOne($uuid)")
        return HttpResponse.ok(resultPdfStorage[UUID.fromString(uuid)])
    }

    private fun convertFieldsToMap(data: String): Map<String, String> {
        logger.trace { "Converting data to map..." }
        logger.trace { "Using data: $data" }
        val map = mutableMapOf<String, String>()

        data.lines()
            .map { line ->
                run {
                    logger.trace { "Converting line '$line'..." }
                    val splits = line.split('=', limit = 2)
                    map[splits[0]] = splits[1]
                }
            }

        logger.trace { "Converted data to map: $map" }
        return map.toMap()
    }
}