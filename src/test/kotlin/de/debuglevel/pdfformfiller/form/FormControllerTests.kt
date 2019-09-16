package de.debuglevel.pdfformfiller.form

import de.debuglevel.pdfformfiller.PdfUtils
import io.micronaut.test.annotation.MicronautTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.util.*
import javax.inject.Inject

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FormControllerTests {

    @Inject
    lateinit var controller: FormController

    @Test
    fun `updating an item works at all`() {
        //println("'${PdfUtils.getMinimalPdf()}'")

        // Arrange
        val form =
            FormRequest(name = "Test", pdf = Base64.getEncoder().encodeToString(PdfUtils.getMinimalPdf().toByteArray()))
        val addedForm = controller.postOne(form)

        // Act
        val retrievedForm = controller.getOne(addedForm.id.toString())

        val updateForm = FormRequest(
            name = "Test2",
            pdf = Base64.getEncoder().encodeToString(PdfUtils.getMinimalPdf().toByteArray())
        )

        val updatedForm = controller.putOne(retrievedForm?.id!!, updateForm)

        // Assert
        val retrieved2Form = controller.getOne(updatedForm.id.toString())
        assertThat(retrieved2Form?.name).isEqualTo("Test2")
    }
}