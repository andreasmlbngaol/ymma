package org.andreasmlbngaol.ymma.auth.domain

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val id: Long,
    val name: String,
    val email: String,
    val username: String,
    val isVerified: Boolean,
    val imageUrl: String? = null,
    val accessToken: String? = null
)
