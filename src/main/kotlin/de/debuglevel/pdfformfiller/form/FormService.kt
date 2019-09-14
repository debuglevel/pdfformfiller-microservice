package de.debuglevel.pdfformfiller.form

import mu.KotlinLogging
import java.time.LocalDateTime
import java.util.*
import javax.inject.Singleton

@Singleton
class FormService(
    private val formRepository: FormRepository,
    private val pdfValidator: PdfValidator
) {
    private val logger = KotlinLogging.logger {}

    fun retrieve(uuid: UUID): Form {
        logger.debug { "Getting form with ID '$uuid'..." }

        val form: Form = formRepository.findById(uuid).orElseThrow { FormNotFoundException(uuid) }

        logger.debug { "Got form with ID '$uuid': $form" }
        return form
    }

    fun save(form: Form): Form {
        logger.debug { "Saving form '$form'..." }

        if (!pdfValidator.validate(form.pdf)) {
            throw InvalidPdfException()
        }

        form.creationDateTime = LocalDateTime.now()
        val savedForm = formRepository.save(form)

        logger.debug { "Saved form: $savedForm" }
        return form
    }

    fun getList(): Set<Form> {
        logger.debug { "Getting all forms..." }

        // TODO: improve performance by not retrieving the data field (and then not using it)
        val forms = formRepository.findAll().toSet()

        logger.debug { "Got all forms" }
        return forms
    }

    class FormNotFoundException(uuid: UUID) : Exception("No form found with ID '$uuid'")
    class InvalidPdfException : Exception("File is not a valid PDF")
}


