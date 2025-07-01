package org.andreasmlbngaol.ymma.auth.domain

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val id: Long,
    val name: String,
    val email: String
)
