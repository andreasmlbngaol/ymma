package org.andreasmlbngaol.ymma.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.Date

object JwtConfig {
    private const val SECRET_KEY: String = "ini-secret-key-untuk-ymma"
    val algorithm: Algorithm = Algorithm.HMAC256(SECRET_KEY)

    const val ACCESS_TOKEN_EXPIRATION_DURATION = 1000L * 60 * 60       // 1 jam
    const val REFRESH_TOKEN_EXPIRATION_DURATION = 1000L * 60 * 60 * 24 * 30 // 30 hari


    fun generateAccessToken(
        userId: Long,
        name: String,
        email: String
    ): String {
        val now = System.currentTimeMillis()
        return JWT.create()
            .withClaim("sub", userId)
            .withClaim("name", name)
            .withClaim("email", email)
            .withIssuedAt(Date(now))
            .withExpiresAt(Date(now + ACCESS_TOKEN_EXPIRATION_DURATION))
            .sign(algorithm)
    }

    fun generateRefreshToken(userId: Long): String {
        val now = System.currentTimeMillis()
        return JWT.create()
            .withClaim("sub", userId)
            .withIssuedAt(Date(now))
            .withExpiresAt(Date(now + REFRESH_TOKEN_EXPIRATION_DURATION))
            .sign(algorithm)
    }

}