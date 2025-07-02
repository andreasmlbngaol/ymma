package org.andreasmlbngaol.ymma.domains.auth

import kotlinx.serialization.Serializable

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

