package de.debuglevel.pdfformfiller.field

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import mu.KotlinLogging
import java.util.*

@Controller("/fields")
class FieldController(private val fieldFinder: FieldFinder) {
    private val logger = KotlinLogging.logger {}

    @Post("/")
    fun postOne(fieldRequest: FieldRequest): HttpResponse<FieldResponse> {
        logger.debug("Called postOne($fieldRequest)")

        return run {
            val pdf = Base64.getDecoder().decode(fieldRequest.pdf).inputStream()
            val fields = fieldFinder.getFields(pdf)
            val fieldResponse = FieldResponse(fields)

            HttpResponse.ok(fieldResponse)
        }
    }
}