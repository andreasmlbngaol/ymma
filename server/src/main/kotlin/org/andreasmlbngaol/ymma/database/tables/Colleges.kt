package org.andreasmlbngaol.ymma.database.tables

import org.jetbrains.exposed.v1.core.dao.id.LongIdTable

object Colleges: LongIdTable("colleges") {
    val name = varchar("name", 255)
    val abbreviation = varchar("abbreviation", 32).nullable()
}