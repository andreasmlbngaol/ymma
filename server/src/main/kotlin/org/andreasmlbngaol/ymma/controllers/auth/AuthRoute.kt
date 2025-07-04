package org.andreasmlbngaol.ymma.controllers.auth

import at.favre.lib.crypto.bcrypt.BCrypt
import io.ktor.http.Cookie
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.RoutingContext
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.andreasmlbngaol.ymma.domains.auth.LoginRequest
import org.andreasmlbngaol.ymma.domains.auth.LoginResponse
import org.andreasmlbngaol.ymma.domains.auth.RegisterRequest
import org.andreasmlbngaol.ymma.database.dao.UsersDao
import org.andreasmlbngaol.ymma.domains.auth.ChangePasswordRequest
import org.andreasmlbngaol.ymma.domains.auth.User
import org.andreasmlbngaol.ymma.plugins.AuthNames.JWT_AUTH
import org.andreasmlbngaol.ymma.utils.respondJson

fun Route.authRoute() {
    route("/auth") {
        post("/login") {
            val payload = call.receive<LoginRequest>()
            val identifier = payload.identifier.trim()

            val user = UsersDao
                .findByUsername(identifier)
                ?: UsersDao.findByEmail(identifier)
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

            val email = payload.email.trim()
            val username = payload.username.trim()
            val password = payload.password.trim()
            val name = payload.name.trim()

            if(!validateEmail(email)) return@post call.respondJson(HttpStatusCode.BadRequest, "Invalid email format")
            if(!validateUsername(username)) return@post call.respondJson(HttpStatusCode.BadRequest, "Invalid username format. Username must be at least 4 characters long and can only contain lowercase letters, numbers, dots, and underscores.")
            if(!validatePassword(password)) return@post call.respondJson(HttpStatusCode.BadRequest, "Invalid password format. Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, and one number.")

            if(UsersDao.existByEmail(email)) return@post call.respondJson(HttpStatusCode.Conflict, "Email already exists")
            if(UsersDao.existByUsername(username)) return@post call.respondJson(HttpStatusCode.Conflict, "Username already exists")

            val hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray())

            val id = UsersDao.create(
                name = name,
                email = email,
                username = username,
                passwordHashed = hashedPassword
            )

            call.respond(
                HttpStatusCode.Created,
                LoginResponse(
                    id = id.value,
                    name = name,
                    email = email,
                    username = username,
                    isVerified = false,
                )
            )
        }

        post("/refresh") {

            val refreshToken = call.request.cookies["refresh_token"]
                ?: return@post call.respondJson(HttpStatusCode.Unauthorized, "Invalid refresh token")

            val userId = JwtConfig.getUserIdFromToken(refreshToken)
                ?: return@post call.respondJson(HttpStatusCode.Unauthorized, "Invalid refresh token")

            val user = UsersDao.findById(userId)
                ?: return@post call.respondJson(HttpStatusCode.Unauthorized, "User not found")

            setCookieAndRespondToken(user)
        }

        authenticate(JWT_AUTH) {
            post("/logout") {
                call.response.cookies.append(
                    Cookie(
                        name = "refresh_token",
                        value = "",
                        path = "/",
                        httpOnly = true,
                        maxAge = 0
                    )
                )

                call.response.cookies.append(
                    Cookie(
                        name = "access_token",
                        value = "",
                        path = "/",
                        httpOnly = true,
                        maxAge = 0
                    )
                )

                call.respondJson(
                    HttpStatusCode.OK,
                    "Logout successful"
                )
            }

            route("/settings") {
                post("/password") {
                    val principal = call.principal<JWTPrincipal>()!!
                    val userId = principal.payload.getClaim("sub").asLong()

                    val payload = call.receive<ChangePasswordRequest>()

                    val oldPassword = payload.oldPassword.trim()
                    val newPassword = payload.newPassword.trim()

                    val isPasswordMatch = BCrypt.verifyer().verify(
                        oldPassword.toByteArray(),
                        UsersDao.findById(userId)?.passwordHashed?.toByteArray()
                    ).verified

                    if(!isPasswordMatch) return@post call.respondJson(HttpStatusCode.BadRequest, "Invalid old password")
                    if(!validatePassword(newPassword)) return@post call.respondJson(HttpStatusCode.BadRequest, "Invalid new password format. Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, and one number.")

                    val hashedPassword = BCrypt.withDefaults().hashToString(12, newPassword.toCharArray())
                    val updatedRows = UsersDao.changePassword(userId, hashedPassword)
                    if(updatedRows == 0) return@post call.respondJson(HttpStatusCode.InternalServerError, "Error while updating password")

                    call.respondJson(
                        HttpStatusCode.OK,
                        "Password updated successfully"
                    )
                }

                post("/profile-picture") {

                }
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
            accessToken = accessToken
        )
    )
}

private fun validateEmail(email: String): Boolean {
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$".toRegex()
    return email.isNotBlank() && email.matches(emailRegex)
}

private fun validatePassword(password: String): Boolean {
    val passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,}$".toRegex()
    return password.matches(passwordRegex)
}

private fun validateUsername(username: String): Boolean {
    val usernameRegex = "^[a-z0-9._]{4,}$".toRegex()
    return username.matches(usernameRegex)
}
