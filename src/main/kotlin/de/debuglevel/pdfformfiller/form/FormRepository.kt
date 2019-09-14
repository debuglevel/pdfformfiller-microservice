package de.debuglevel.pdfformfiller.form

import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository
import java.util.*

@Repository
interface FormRepository : CrudRepository<Form, UUID>