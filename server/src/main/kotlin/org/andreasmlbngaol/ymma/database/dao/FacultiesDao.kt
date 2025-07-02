package org.andreasmlbngaol.ymma.database.dao

import org.andreasmlbngaol.ymma.database.converter.toFaculty
import org.andreasmlbngaol.ymma.database.tables.Faculties
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

object FacultiesDao {
    fun create(
        collegeId: Long,
        name: String,
        abbreviation: String? = null
    ) = transaction {
        Faculties.insertAndGetId {
            it[Faculties.college] = collegeId
            it[Faculties.name] = name
            it[Faculties.abbreviation] = abbreviation
        }
    }

    fun existById(
        id: Long
    ) = transaction {
        Faculties
            .select(Faculties.id)
            .where { Faculties.id eq id }
            .count() > 0
    }

    fun findAllByCollegeId(
        collegeId: Long
    ) = transaction {
        Faculties
            .selectAll()
            .where { Faculties.college eq collegeId }
            .map { it.toFaculty() }
    }
}