package de.debuglevel.pdfformfiller.form

import io.micronaut.test.annotation.MicronautTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import javax.inject.Inject

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FormRepositoryTests {

    @Inject
    lateinit var repository: FormRepository

    @Test
    fun `updating an item works at all`() {
        // Arrange
        val form = Form(id = null, name = "Test", pdf = "xyz".toByteArray())
        val savedForm = repository.save(form)

        // Act
        val retrievedForm = repository.findById(savedForm.id!!).get()
        retrievedForm.name = "Test2"
        val saved2Form = repository.save(retrievedForm)

        // Assert
        val retrieved2Form = repository.findById(savedForm.id!!).get()
        assertThat(retrieved2Form.name).isEqualTo("Test2")
    }
}