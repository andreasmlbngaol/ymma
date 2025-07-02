package org.andreasmlbngaol.ymma.database.tables

import org.jetbrains.exposed.v1.core.dao.id.LongIdTable

object Departments: LongIdTable("departments") {
    val faculty = reference("faculty_id", Faculties)
    val name = varchar("name", 255)
    val abbreviation = varchar("abbreviation", 64).nullable()
}