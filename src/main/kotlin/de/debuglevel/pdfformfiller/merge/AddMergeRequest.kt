package de.debuglevel.pdfformfiller.merge

import java.util.*

data class AddMergeRequest(
    val pdf: String?,
    val pdfId: UUID?,
    val values: String
)
