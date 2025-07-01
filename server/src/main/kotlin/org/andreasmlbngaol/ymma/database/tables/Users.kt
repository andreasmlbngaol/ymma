package org.andreasmlbngaol.ymma.database.tables

import org.jetbrains.exposed.v1.core.dao.id.LongIdTable

object Users: LongIdTable("users") {
    val name = varchar("name", 40)
    val email = varchar("email", 60).uniqueIndex()
    val username = varchar("username", 32).uniqueIndex()
    val passwordHashed = varchar("password_hashed", 255)
    val isVerified = bool("is_verified").default(false)
    val imageUrl = varchar("image_url", 255).nullable()
}