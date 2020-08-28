package de.debuglevel.pdfformfiller.form

import de.debuglevel.pdfformfiller.field.TestDataProvider
import io.micronaut.test.annotation.MicronautTest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import javax.inject.Inject

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PdfValidatorTest {

    @Inject
    lateinit var service: PdfValidator

    @ParameterizedTest
    @MethodSource("formProvider")
    fun `validate valid PDFs`(testForm: TestDataProvider.TestForm) {
        // Arrange
        val inputStream = testForm.pdfPath.toFile().inputStream()

        // Act
        val isValid = service.validate(inputStream)

        // Assert
        Assertions.assertThat(isValid).isTrue
    }

    @Test
    fun `validate invalid PDFs`() {
        // Arrange
        val inputStream = "foobar".byteInputStream()

        // Act
        val isValid = service.validate(inputStream)

        // Assert
        Assertions.assertThat(isValid).isFalse
    }

    fun formProvider() = TestDataProvider.formProvider()
}