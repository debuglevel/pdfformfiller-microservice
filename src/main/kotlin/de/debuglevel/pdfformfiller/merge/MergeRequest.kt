package de.debuglevel.pdfformfiller.merge

import java.util.*

data class MergeRequest(
    val pdf: String?,
    val pdfId: UUID?,
    val values: String
)
