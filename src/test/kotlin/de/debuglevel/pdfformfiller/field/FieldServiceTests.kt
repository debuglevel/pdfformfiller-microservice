package de.debuglevel.pdfformfiller.field

import io.micronaut.test.annotation.MicronautTest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import javax.inject.Inject

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FieldServiceTests {
    @Inject
    lateinit var service: FieldService

    @ParameterizedTest
    @MethodSource("formProvider")
    fun `get fields`(testForm: TestDataProvider.TestForm) {
        // Arrange
        val inputStream = testForm.pdfPath.toFile().inputStream()

        // Act
        val fields = service.getFields(inputStream)

        // Assert
        Assertions.assertThat(fields).containsExactly(*testForm.fields.toTypedArray())
    }

    fun formProvider() = TestDataProvider.formProvider()
}