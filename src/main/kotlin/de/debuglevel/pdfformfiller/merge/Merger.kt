package de.debuglevel.pdfformfiller.merge

import java.io.InputStream
import java.io.OutputStream

interface Merger {
    fun merge(
        pdf: InputStream,
        data: Map<String, String>,
        resultPdf: OutputStream,
        flatten: Boolean = true
    )
}