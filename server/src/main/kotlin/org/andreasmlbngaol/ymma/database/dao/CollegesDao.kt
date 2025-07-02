package org.andreasmlbngaol.ymma.database.dao

import org.andreasmlbngaol.ymma.database.tables.Colleges
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

object CollegesDao {
    fun create(
        name: String,
        abbreviation: String? = null
    ) = transaction {
        Colleges.insertAndGetId {
            it[Colleges.name] = name
            it[Colleges.abbreviation] = abbreviation
        }
    }

    fun existById(
        id: Long
    ) = transaction {
        Colleges
            .select(Colleges.id)
            .where { Colleges.id eq id }
            .count() > 0
    }
}