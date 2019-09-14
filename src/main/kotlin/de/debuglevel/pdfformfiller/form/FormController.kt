package de.debuglevel.pdfformfiller.form

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import mu.KotlinLogging
import java.util.*

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/forms")
class FormController(private val formService: FormService) {
    private val logger = KotlinLogging.logger {}

    @Get("/")
    fun getList(): Set<FormResponse> {
        logger.debug("Called getList()")
        return formService.getList()
            .map {
                FormResponse(
                    id = it.id,
                    creationDateTime = it.creationDateTime,
                    name = it.name,
                    pdf = null
                )
            }
            .toSet()
    }

    @Get("/{uuid}")
    fun getOne(uuid: String): FormResponse? {
        logger.debug("Called getOne($uuid)")
        val form = formService.retrieve(UUID.fromString(uuid))
        return FormResponse(
            id = form.id,
            name = form.name,
            creationDateTime = form.creationDateTime,
            pdf = Base64.getEncoder().encodeToString(form.pdf)
        )
    }

    @Post("/")
    fun postOne(formRequest: FormRequest): FormResponse {
        logger.debug("Called postOne($formRequest)")
        val form = Form(
            name = formRequest.name,
            pdf = Base64.getDecoder().decode(formRequest.pdf)
        )
        val savedForm = formService.save(form)
        return FormResponse(
            id = savedForm.id,
            name = savedForm.name,
            pdf = Base64.getEncoder().encodeToString(savedForm.pdf),
            creationDateTime = savedForm.creationDateTime
        )
    }
}