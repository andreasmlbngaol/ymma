package org.andreasmlbngaol.ymma.auth.domain

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val name: String,
    val email: String,
    val username: String,
    val password: String
)
