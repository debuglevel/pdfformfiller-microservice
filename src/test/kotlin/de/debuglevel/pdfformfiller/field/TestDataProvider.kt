package de.debuglevel.pdfformfiller.field

import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Stream

object TestDataProvider {
    data class TestForm(val pdfPath: Path, val fields: Set<String>)

    fun formProvider() = Stream.of(
        TestForm(Paths.get("example/forms/PDF form FDF.pdf"), setOf("Field 1", "Field 2")),
        TestForm(Paths.get("example/forms/PDF form HTML.pdf"), setOf("Field 1", "Field 2")),
        TestForm(Paths.get("example/forms/PDF form PDF PDF-A.pdf"), setOf("Field 1", "Field 2")),
        TestForm(Paths.get("example/forms/PDF form PDF.pdf"), setOf("Field 1", "Field 2")),
        TestForm(Paths.get("example/forms/PDF form XML.pdf"), setOf("Field 1", "Field 2")),
    )
}