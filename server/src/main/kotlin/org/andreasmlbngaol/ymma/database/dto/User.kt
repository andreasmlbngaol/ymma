package org.andreasmlbngaol.ymma.database.dto

import kotlinx.serialization.Serializable
import org.andreasmlbngaol.ymma.database.tables.Users
import org.jetbrains.exposed.v1.core.ResultRow

@Serializable
data class User(
    val id: Long,
    val name: String,
    val email: String,
    val username: String,
    val passwordHashed: String,
    val isVerified: Boolean,
    val imageUrl: String? = null,
    val isActive: Boolean = true
)

fun ResultRow.toUser() = User(
    id = this[Users.id].value,
    name = this[Users.name],
    email = this[Users.email],
    username = this[Users.username],
    passwordHashed = this[Users.passwordHashed],
    isVerified = this[Users.isVerified],
    imageUrl = this[Users.imageUrl],
    isActive = this[Users.isActive]
)