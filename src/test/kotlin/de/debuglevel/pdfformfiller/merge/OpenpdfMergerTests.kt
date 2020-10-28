package de.debuglevel.pdfformfiller.merge

import com.lowagie.text.pdf.PdfReader
import com.lowagie.text.pdf.parser.PdfTextExtractor
import de.debuglevel.pdfformfiller.field.TestDataProvider
import io.micronaut.test.annotation.MicronautTest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OpenpdfMergerTests {
    @Inject
    lateinit var service: OpenpdfMerger

    @ParameterizedTest
    @MethodSource("formProvider")
    fun `fill values into form`(testForm: TestDataProvider.TestForm) {
        // Arrange
        val inputStream = testForm.pdfPath.toFile().inputStream()
        val values = testForm.fields.associateBy({ it }, { "$it Value" })
        val outputStream = ByteArrayOutputStream()
        val flatten = true // PdfTextExtractor does not find text on flatten = false

        // Act
        service.merge(inputStream, values, outputStream, flatten)

        // Assert
        val bytes = outputStream.toByteArray()
        val text = PdfTextExtractor(PdfReader(bytes)).getTextFromPage(1)
        // Assertions.assertThat(text).contains(values.values) // TODO: use this line instead when OpenPDF 1.3.23 is released
        Assertions.assertThat(text).contains(values.values.map {
            it.replace(
                " ",
                "  "
            )
        }) // TODO: PdfTextExtractor adds two spaces between words instead of one; this line is a workaround to match this behaviour.
    }

    fun formProvider() = TestDataProvider.formProvider()
}
