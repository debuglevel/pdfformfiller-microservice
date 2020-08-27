package de.debuglevel.pdfformfiller.form

import de.debuglevel.pdfformfiller.PdfUtils
import io.micronaut.test.annotation.MicronautTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import javax.inject.Inject

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FormServiceTests {

    @Inject
    lateinit var service: FormService

    @Test
    fun `updating an item works at all`() {
        //println("'${PdfUtils.getMinimalPdf()}'")

        // Arrange
        val form = Form(id = null, name = "Test", pdf = PdfUtils.getMinimalPdf().toByteArray())
        val addedForm = service.add(form)

        // Act
        val retrievedForm = service.retrieve(addedForm.id!!)
        retrievedForm.name = "Test2"
        val updatedForm = service.update(retrievedForm.id!!, retrievedForm)

        // Assert
        val retrieved2Form = service.retrieve(addedForm.id!!)
        assertThat(retrieved2Form.name).isEqualTo("Test2")
    }
}