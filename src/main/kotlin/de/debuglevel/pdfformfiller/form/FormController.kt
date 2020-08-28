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
            val form = formService.retrieve(id)

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

    // TODO: fails due to: Unexpected error occurred: org.hibernate.PersistentObjectException: detached entity passed to persist: de.debuglevel.pdfformfiller.form.Form
    @Put("/{id}")
    fun putOne(id: UUID, addFormRequest: AddFormRequest): FormResponse {
        logger.debug("Called putOne($id, $addFormRequest)")
        val form = Form(
            id = null,
            name = addFormRequest.name,
            pdf = Base64.getDecoder().decode(addFormRequest.pdf)
        )
        // TODO: this can throw a InvalidPdfException; should be handled appropriately
        val savedForm = formService.update(id, form)
        val formResponse = FormResponse(
            id = savedForm.id!!,
            name = savedForm.name,
            pdf = Base64.getEncoder().encodeToString(savedForm.pdf),
            createdOn = savedForm.createdOn,
            lastModified = savedForm.lastModified
        )
        return formResponse
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