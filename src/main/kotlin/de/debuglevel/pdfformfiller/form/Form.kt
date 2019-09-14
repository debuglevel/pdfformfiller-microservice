package de.debuglevel.pdfformfiller.form

import java.time.LocalDateTime
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Lob

@Entity
data class Form(
    @Id
    @GeneratedValue
    var id: UUID? = null,
    var name: String,
    var creationDateTime: LocalDateTime? = null,
    @Lob
    var pdf: ByteArray
)