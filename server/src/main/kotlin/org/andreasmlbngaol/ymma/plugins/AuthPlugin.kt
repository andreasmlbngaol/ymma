package org.andreasmlbngaol.ymma.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt

fun Application.authenticationPlugin() {
    val secretKey = environment.config.config("ktor.jwt").property("secretKey").getString()
    install(Authentication) {
        jwt(AuthNames.JWT_AUTH) {
            realm = "Access to the application"
            verifier(
                JWT
                    .require(Algorithm.HMAC256(secretKey))
                    .build()
            )
            validate { credential ->
                val payload = credential.payload
                val id = payload.getClaim("sub").asLong()

                if (id != null) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }
    }
}

object AuthNames {
    const val JWT_AUTH = "basic-auth"
}