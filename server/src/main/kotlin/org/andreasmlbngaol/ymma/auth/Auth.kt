package org.andreasmlbngaol.ymma.auth

import at.favre.lib.crypto.bcrypt.BCrypt
import io.ktor.http.Cookie
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.RoutingContext
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.andreasmlbngaol.ymma.auth.domain.LoginRequest
import org.andreasmlbngaol.ymma.auth.domain.LoginResponse
import org.andreasmlbngaol.ymma.auth.domain.RegisterRequest
import org.andreasmlbngaol.ymma.database.dao.UsersDao
import org.andreasmlbngaol.ymma.database.dto.User
import org.andreasmlbngaol.ymma.utils.respondJson

fun Route.authRoute() {
    route("/auth") {
        post("/login") {
            val payload = call.receive<LoginRequest>()

            val user = UsersDao
                .findByUsername(payload.identifier)
                ?: UsersDao.findByEmail(payload.identifier)
                ?: return@post call.respondJson(HttpStatusCode.Unauthorized, "Invalid credentials")

            val isPasswordMatch = BCrypt.verifyer().verify(
                payload.password.toByteArray(),
                user.passwordHashed.toByteArray()
            ).verified

            if(!isPasswordMatch) return@post call.respondJson(HttpStatusCode.Unauthorized, "Invalid credentials")

            setCookieAndRespondToken(user)
        }

        post("/register") {
            val payload = call.receive<RegisterRequest>()

            if(UsersDao.existByEmail(payload.email)) return@post call.respondJson(HttpStatusCode.Conflict, "Email already exists")
            if(UsersDao.existByUsername(payload.username)) return@post call.respondJson(HttpStatusCode.Conflict, "Username already exists")

            val hashedPassword = BCrypt.withDefaults().hashToString(12, payload.password.toCharArray())

            val id = UsersDao.create(
                name = payload.name.trim(),
                email = payload.email.trim(),
                username = payload.username.trim(),
                passwordHashed = hashedPassword
            )

            call.respond(
                HttpStatusCode.Created,
                LoginResponse(
                    id = id.value,
                    name = payload.name,
                    email = payload.email,
                    username = payload.username,
                    isVerified = false
                )
            )
        }

        authenticate("auth-jwt") {
            post("/refresh") {

                val refreshToken = call.request.cookies["refresh_token"]
                    ?: return@post call.respondJson(HttpStatusCode.Unauthorized, "Invalid refresh token")

                val userId = JwtConfig.getUserIdFromToken(refreshToken)
                    ?: return@post call.respondJson(HttpStatusCode.Unauthorized, "Invalid refresh token")

                val user = UsersDao.findById(userId)
                    ?: return@post call.respondJson(HttpStatusCode.Unauthorized, "User not found")

                setCookieAndRespondToken(user)
            }
        }
    }
}

suspend fun RoutingContext.setCookieAndRespondToken(user: User) {
    val accessToken = JwtConfig.generateAccessToken(user.id, user.name, user.email, user.username)
    val refreshToken = JwtConfig.generateRefreshToken(user.id)

    call.response.cookies.append(
        Cookie(
            name = "refresh_token",
            value = refreshToken,
            path = "/",
            httpOnly = true,
            maxAge = JwtConfig.REFRESH_TOKEN_EXPIRATION_DURATION_IN_SECOND
        )
    )

    call.response.cookies.append(
        Cookie(
            name = "access_token",
            value = accessToken,
            path = "/",
            httpOnly = true,
            maxAge = JwtConfig.ACCESS_TOKEN_EXPIRATION_DURATION_IN_SECOND
        )
    )

    call.respond(
        LoginResponse(
            id = user.id,
            name = user.name,
            email = user.email,
            username = user.username,
            isVerified = user.isVerified,
            imageUrl = user.imageUrl,
        )
    )
}