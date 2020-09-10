package de.debuglevel.pdfformfiller.merge

import io.micronaut.data.annotation.DateCreated
import io.micronaut.data.annotation.DateUpdated
import java.time.LocalDateTime
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Lob

@Entity
data class Merge(
    @Id
    @GeneratedValue
    var id: UUID?,
    @Lob
    var pdf: ByteArray,
    @DateCreated
    var createdOn: LocalDateTime = LocalDateTime.now(),
    @DateUpdated
    var lastModified: LocalDateTime = LocalDateTime.now(),
)