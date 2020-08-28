package de.debuglevel.pdfformfiller.field

import com.lowagie.text.pdf.PdfReader
import mu.KotlinLogging
import java.io.InputStream
import javax.inject.Singleton

@Singleton
class FieldService {
    private val logger = KotlinLogging.logger {}

    fun getFields(pdf: InputStream): List<String> {
        logger.debug { "Getting fields in PDF..." }

        val pdfReader = PdfReader(pdf)
        val fieldKeys = pdfReader.acroFields.allFields.keys.sortedBy { it }
        pdfReader.close()

        logger.debug { "Got fields in PDF: $fieldKeys" }
        return fieldKeys
    }
}