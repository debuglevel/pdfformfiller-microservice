package de.debuglevel.pdfformfiller.form

import com.lowagie.text.pdf.PdfReader
import mu.KotlinLogging
import javax.inject.Singleton

@Singleton
class PdfValidator {
    private val logger = KotlinLogging.logger {}

    /**
     * @implNote: Probably not the very best PDF validation check, but should catch the worst cases
     */
    fun validate(pdf: ByteArray): Boolean {
        logger.debug { "Validating PDF..." }
        val isValidPdf = try {
            val pdfReader = PdfReader(pdf)
            pdfReader.numberOfPages > 0
        } catch (e: Exception) {
            logger.debug(e) { "Validating PDF failed while parsing PDF" }
            false
        }

        logger.debug { "Validated PDF: $isValidPdf" }
        return isValidPdf
    }
}
