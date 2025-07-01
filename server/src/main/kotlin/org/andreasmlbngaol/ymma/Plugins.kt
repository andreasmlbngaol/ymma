package org.andreasmlbngaol.ymma

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import kotlinx.serialization.json.Json

fun Application.authPlugin() {
    install(Authentication) {
        jwt("auth-jwt") {
            realm = "Access to the application"
            verifier(
                JWT
                    .require(Algorithm.HMAC256("secret"))
                    .build()
            )
            validate { credential ->
                if (credential.payload.getClaim("username").asString().isNotEmpty()) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }
    }
}

fun Application.contentNegotiationPlugin() {
    install(ContentNegotiation) {
        json(
            json = Json {
                prettyPrint = true
                isLenient = true
            },
            contentType = ContentType.Application.Json
        )
    }
}