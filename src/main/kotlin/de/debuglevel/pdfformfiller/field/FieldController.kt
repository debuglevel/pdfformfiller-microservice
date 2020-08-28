package de.debuglevel.pdfformfiller.field

import de.debuglevel.pdfformfiller.form.FormService
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import mu.KotlinLogging
import java.util.*

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/fields")
class FieldController(
    private val fieldService: FieldService,
    private val formService: FormService
) {
    private val logger = KotlinLogging.logger {}

    @Post("/")
    fun postOne(fieldRequest: FieldRequest): HttpResponse<FieldResponse> {
        logger.debug("Called postOne($fieldRequest)")

        return run {
            val pdf = Base64.getDecoder().decode(fieldRequest.pdf).inputStream()
            val fields = fieldService.getFields(pdf)
            val fieldResponse = FieldResponse(fields)

            HttpResponse.ok(fieldResponse)
        }
    }

    @Get("/{id}")
    fun getOne(id: UUID): HttpResponse<FieldResponse> {
        logger.debug("Called getOne($id)")

        return run {
            val pdf = formService.retrieve(id).pdf.inputStream()
            val fields = fieldService.getFields(pdf)
            val fieldResponse = FieldResponse(fields)

            HttpResponse.ok(fieldResponse)
        }
    }
}