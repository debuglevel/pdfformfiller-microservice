package de.debuglevel.pdfformfiller.merge

import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository
import java.util.*

@Repository
interface MergeRepository : CrudRepository<Merge, UUID>