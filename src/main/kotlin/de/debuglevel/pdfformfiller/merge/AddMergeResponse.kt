package de.debuglevel.pdfformfiller.merge

import java.util.*

data class AddMergeResponse(
    val id: UUID
) {
    constructor(merge: Merge) : this(
        id = merge.id!!
    )
}
