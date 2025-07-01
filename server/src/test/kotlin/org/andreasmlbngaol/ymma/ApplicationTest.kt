package org.andreasmlbngaol.ymma

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.andreasmlbngaol.ymma.database.DatabaseFactory
import kotlin.test.*

class ApplicationTest {

    @Test
    fun testRoot() = testApplication {
        application {
            DatabaseFactory.init(environment.config)

            module()
        }

        val response = client.post("/api") {
            contentType(ContentType.Application.Json)
        }

        println(response.bodyAsText())

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Ktor: ${Greeting().greet()}", response.bodyAsText())
    }
}