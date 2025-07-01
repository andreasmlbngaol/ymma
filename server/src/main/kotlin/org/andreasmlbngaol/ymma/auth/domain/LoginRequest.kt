package org.andreasmlbngaol.ymma.auth.domain

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val identifier: String,
    val password: String
)
