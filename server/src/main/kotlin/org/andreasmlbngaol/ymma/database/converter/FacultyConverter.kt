package org.andreasmlbngaol.ymma.database.converter

import org.andreasmlbngaol.ymma.database.tables.Faculties
import org.andreasmlbngaol.ymma.domains.faculty.Faculty
import org.jetbrains.exposed.v1.core.ResultRow

fun ResultRow.toFaculty() = Faculty(
    id = this[Faculties.id].value,
    name = this[Faculties.name],
    abbreviation = this[Faculties.abbreviation]
)