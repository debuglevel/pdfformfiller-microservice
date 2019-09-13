package de.debuglevel.pdfformfiller.merge

import com.lowagie.text.pdf.PdfReader
import com.lowagie.text.pdf.PdfStamper
import mu.KotlinLogging
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Singleton

@Singleton
class OpenpdfMerger : Merger {
    private val logger = KotlinLogging.logger {}

    override fun merge(
        pdf: InputStream,
        data: Map<String, String>,
        resultPdf: OutputStream,
        flatten: Boolean
    ) {
        logger.debug { "Merging PDF..." }

        val pdfReader = PdfReader(pdf)
        val pdfStamper = PdfStamper(pdfReader, resultPdf)

        // fill in form values
        val form = pdfStamper.acroFields
        data.forEach { (key, value) ->
            run {
                logger.debug { "Setting field '$key'='$value'" }
                form.setField(key, value)
            }
        }

        // remove form controls and only retain the values
        pdfStamper.setFormFlattening(flatten)

        pdfStamper.close()
        pdfReader.close()

        logger.debug { "Merged PDF" }
    }
}