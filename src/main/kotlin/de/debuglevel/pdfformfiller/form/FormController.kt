package de.debuglevel.pdfformfiller.form

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import mu.KotlinLogging
import java.util.*

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/forms")
class FormController(private val formService: FormService) {
    private val logger = KotlinLogging.logger {}

    @Get("/")
    fun getList(): HttpResponse<*> {
        logger.debug("Called getList()")

        return try {
            val formResponses = formService.getList()
                .map {
                    FormResponse(
                        id = it.id!!,
                        name = it.name,
                        pdf = null,
                        createdOn = it.createdOn,
                        lastModified = it.lastModified,
                    )
                }
                .toSet()
            HttpResponse.ok(formResponses)
        } catch (e: Exception) {
            logger.error(e) { "Unhandled exception" }
            HttpResponse.serverError("Unhandled exception: " + e.stackTrace)
        }
    }

    @Get("/{id}")
    fun getOne(id: UUID): HttpResponse<*> {
        logger.debug("Called getOne($id)")
        return try {
            val form = formService.get(id)

            val formResponse = FormResponse(
                id = form.id!!,
                name = form.name,
                pdf = Base64.getEncoder().encodeToString(form.pdf),
                createdOn = form.createdOn,
                lastModified = form.lastModified
            )

            HttpResponse.ok(formResponse)
        } catch (e: FormService.FormNotFoundException) {
            HttpResponse.notFound("Form $id not found.")
        } catch (e: Exception) {
            logger.error(e) { "Unhandled exception" }
            HttpResponse.serverError("Unhandled exception: " + e.stackTrace)
        }
    }

    @Put("/{id}")
    fun putOne(id: UUID, updateFormRequest: UpdateFormRequest): HttpResponse<*> {
        logger.debug("Called putOne($id, $updateFormRequest)")
        return try {
            val form = Form(
                id = null,
                name = updateFormRequest.name,
                pdf = Base64.getDecoder().decode(updateFormRequest.pdf)
            )

            val updatedForm = formService.update(id, form)

            val formResponse = FormResponse(updatedForm)
            HttpResponse.ok(formResponse)
        } catch (e: FormService.FormNotFoundException) {
            HttpResponse.notFound("Form $id not found.")
        } catch (e: FormService.InvalidPdfException) {
            HttpResponse.badRequest("The given PDF is invalid.")
        } catch (e: Exception) {
            logger.error(e) { "Unhandled exception" }
            HttpResponse.serverError("Unhandled exception: " + e.stackTrace)
        }
    }

    @Post("/")
    fun postOne(addFormRequest: AddFormRequest): HttpResponse<*> {
        logger.debug("Called postOne($addFormRequest)")
        return try {
            val form = Form(
                id = null,
                name = addFormRequest.name,
                pdf = Base64.getDecoder().decode(addFormRequest.pdf)
            )

            val savedForm = formService.add(form)

            val formResponse = FormResponse(
                id = savedForm.id!!,
                name = savedForm.name,
                pdf = Base64.getEncoder().encodeToString(savedForm.pdf),
                createdOn = savedForm.createdOn,
                lastModified = savedForm.lastModified,
            )

            HttpResponse.created(formResponse)
        } catch (e: FormService.InvalidPdfException) {
            HttpResponse.badRequest("The given PDF is invalid.")
        } catch (e: Exception) {
            logger.error(e) { "Unhandled exception" }
            HttpResponse.serverError("Unhandled exception: " + e.stackTrace)
        }
    }
}