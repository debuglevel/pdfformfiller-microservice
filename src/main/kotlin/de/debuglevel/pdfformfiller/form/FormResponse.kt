package de.debuglevel.pdfformfiller.form

import java.time.LocalDateTime
import java.util.*

data class FormResponse(
    val id: UUID,
    val name: String,
    val pdf: String?,
    val createdOn: LocalDateTime,
    val lastModified: LocalDateTime,
) {
    constructor(form: Form) : this(
        id = form.id!!,
        name = form.name,
        pdf = Base64.getEncoder().encodeToString(form.pdf),
        createdOn = form.createdOn,
        lastModified = form.lastModified,
    )
}
