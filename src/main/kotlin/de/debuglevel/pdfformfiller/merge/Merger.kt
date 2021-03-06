package de.debuglevel.pdfformfiller.merge

import java.io.InputStream
import java.io.OutputStream

interface Merger {
    /**
     * @param pdf the PDF form to be filled
     * @param values the values to be filled in the forms (where the key is the control name and the value is the value to fill in)
     * @param resultPdf the modified PDF with the values filled in
     * @param flatten true if the form controls should be removed (i.e. the PDF becomes read only)
     */
    fun merge(
        pdf: InputStream,
        values: Map<String, String>,
        resultPdf: OutputStream,
        flatten: Boolean = true
    )
}