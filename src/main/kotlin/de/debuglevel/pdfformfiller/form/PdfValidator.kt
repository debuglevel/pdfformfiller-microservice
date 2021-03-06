package de.debuglevel.pdfformfiller.form

import com.lowagie.text.pdf.PdfReader
import mu.KotlinLogging
import java.io.InputStream
import javax.inject.Singleton

@Singleton
class PdfValidator {
    private val logger = KotlinLogging.logger {}

    /**
     * @implNote: Probably not the very best PDF validation check, but should catch the worst cases
     */
    fun validate(pdf: InputStream): Boolean {
        logger.debug { "Validating PDF..." }
        val isValidPdf = try {
            val pdfReader = PdfReader(pdf)
            val isValid = pdfReader.numberOfPages > 0
            logger.debug { "Parsing PDF worked with ${pdfReader.numberOfPages} pages; assuming PDF is valid: $isValid" }
            isValid
        } catch (e: Exception) {
            logger.debug { "Parsing PDF failed; assuming PDF is not valid: ${e.stackTrace}" }
            false
        }

        logger.debug { "Validated PDF: $isValidPdf" }
        return isValidPdf
    }
}
