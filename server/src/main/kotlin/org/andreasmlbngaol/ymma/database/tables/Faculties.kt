package org.andreasmlbngaol.ymma.database.tables

import org.jetbrains.exposed.v1.core.dao.id.LongIdTable

object Faculties: LongIdTable("faculties") {
    val college = reference("college_id", Colleges)
    val name = varchar("name", 255)
    val abbreviation = varchar("abbreviation", 64).nullable()
}