package org.andreasmlbngaol.ymma

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.statuspages.StatusPages
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy
import org.andreasmlbngaol.ymma.utils.respondJson

fun Application.authenticationPlugin() {
    val secretKey = environment.config.config("ktor.jwt").property("secretKey").getString()
    install(Authentication) {
        jwt("auth") {
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

@OptIn(ExperimentalSerializationApi::class)
fun Application.contentNegotiationPlugin() {
    install(ContentNegotiation) {
        json(
            json = Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
                namingStrategy = JsonNamingStrategy.SnakeCase
            },
            contentType = ContentType.Application.Json
        )
    }
}

fun Application.statusPagesPlugin() {
    install(StatusPages) {
        exception<BadRequestException> { call, cause ->
            call.respondJson(HttpStatusCode.BadRequest, "Invalid Request Body")
        }
        exception<Throwable> { call, cause ->
            call.respondJson(HttpStatusCode.InternalServerError, cause::class.qualifiedName ?: "Unknown error")
        }
    }
}