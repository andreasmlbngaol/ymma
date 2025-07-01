package org.andreasmlbngaol.ymma.database.dao

import org.andreasmlbngaol.ymma.database.dto.toUser
import org.andreasmlbngaol.ymma.database.tables.Users
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

object UsersDao {
    fun findById(id: Long) = transaction {
        Users.selectAll()
            .where { Users.id eq id }
            .mapNotNull { it.toUser() }
            .singleOrNull()
    }

    fun findByEmail(email: String) = transaction {
        Users.selectAll()
            .where { Users.email eq email }
            .mapNotNull { it.toUser() }
            .singleOrNull()
    }

    fun findByUsername(username: String) = transaction {
        Users.selectAll()
            .where { Users.username eq username }
            .mapNotNull { it.toUser() }
            .singleOrNull()
    }

    fun existByEmail(email: String) = transaction {
        Users
            .select(Users.id)
            .where { Users.email eq email }
            .count() > 0
    }

    fun existByUsername(username: String) = transaction {
        Users
            .select(Users.id)
            .where { Users.username eq username }
            .count() > 0
    }


    fun create(
        name: String,
        email: String,
        username: String,
        passwordHashed: String
    ) = transaction {
            Users.insertAndGetId {
                it[Users.name] = name
                it[Users.email] = email
                it[Users.username] = username
                it[Users.passwordHashed] = passwordHashed
            }
        }
}