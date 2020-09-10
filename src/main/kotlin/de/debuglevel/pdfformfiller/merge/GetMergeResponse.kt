package de.debuglevel.pdfformfiller.merge

import java.time.LocalDateTime
import java.util.*

data class GetMergeResponse(
    val id: UUID,
    val createdOn: LocalDateTime,
    val lastModified: LocalDateTime
) {
    constructor(merge: Merge) : this(
        id = merge.id!!,
        createdOn = merge.createdOn,
        lastModified = merge.lastModified,
    )
}
