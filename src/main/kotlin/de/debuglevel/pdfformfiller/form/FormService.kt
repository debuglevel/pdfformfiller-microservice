package de.debuglevel.pdfformfiller.form

import mu.KotlinLogging
import java.util.*
import javax.inject.Singleton

@Singleton
class FormService(
    private val formRepository: FormRepository,
    private val pdfValidator: PdfValidator
) {
    private val logger = KotlinLogging.logger {}

    fun get(id: UUID): Form {
        logger.debug { "Getting form with ID '$id'..." }

        val form: Form = formRepository.findById(id).orElseThrow { FormNotFoundException(id) }

        logger.debug { "Got form with ID '$id': $form" }
        return form
    }

    fun add(form: Form): Form {
        logger.debug { "Saving form '$form'..." }

        if (!pdfValidator.validate(form.pdf.inputStream())) {
            throw InvalidPdfException()
        }

        val addedForm = formRepository.save(form)

        logger.debug { "Saved form: $addedForm" }
        return form
    }

    fun update(id: UUID, form: Form): Form {
        logger.debug { "Updating form '$form' with ID $id..." }

        if (!pdfValidator.validate(form.pdf.inputStream())) {
            throw InvalidPdfException()
        }

        val existingForm = this.get(id).apply {
            name = form.name
            pdf = form.pdf
        }

        val updatedForm = formRepository.update(existingForm)

        logger.debug { "Updated form with ID $id: $updatedForm" }
        return updatedForm
    }

    fun getList(): Set<Form> {
        logger.debug { "Getting all forms..." }

        // TODO: improve performance by not retrieving the data field (and then not using it)
        val forms = formRepository.findAll().toSet()

        logger.debug { "Got all forms" }
        return forms
    }

    class FormNotFoundException(id: UUID) : Exception("No form found with ID '$id'")
    class InvalidPdfException : Exception("File is not a valid PDF")
}


