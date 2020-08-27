package de.debuglevel.pdfformfiller.form

import io.micronaut.data.annotation.DateCreated
import io.micronaut.data.annotation.DateUpdated
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
    var id: UUID?,
    var name: String,
    @Lob
    var pdf: ByteArray,
    @DateCreated
    var createdOn: LocalDateTime = LocalDateTime.now(),
    @DateUpdated
    var lastModified: LocalDateTime = LocalDateTime.now(),
)