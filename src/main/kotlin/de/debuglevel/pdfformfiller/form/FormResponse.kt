package de.debuglevel.pdfformfiller.form

import java.time.LocalDateTime
import java.util.*

data class FormResponse(
    val uuid: UUID?,
    val name: String,
    val creationDateTime: LocalDateTime?,
    val pdf: String?
)
