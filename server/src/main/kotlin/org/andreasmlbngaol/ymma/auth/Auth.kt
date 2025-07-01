package org.andreasmlbngaol.ymma.auth

import io.ktor.http.Cookie
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.andreasmlbngaol.ymma.auth.domain.LoginResponse

fun Route.authRoute() {
    route("/auth") {
        post("/login") {
            val userId = 1L
            val name = "Test"
            val email = "test@example.com"

            val accessToken = JwtConfig.generateAccessToken(userId, name, email)
            val refreshToken = JwtConfig.generateRefreshToken(userId)

            call.response.cookies.append(
                Cookie(
                    name = "refresh_token",
                    value = refreshToken,
                    path = "/",
                    httpOnly = true,
                    maxAge = JwtConfig.REFRESH_TOKEN_EXPIRATION_DURATION.toInt()
                )
            )

            call.response.cookies.append(
                Cookie(
                    name = "access_token",
                    value = accessToken,
                    path = "/",
                    httpOnly = true,
                    maxAge = JwtConfig.ACCESS_TOKEN_EXPIRATION_DURATION.toInt()
                )
            )

            call.respond(LoginResponse(userId, name, email))
        }

        post("/register") {
            call.respondText("Register")
        }
    }
}