package org.andreasmlbngaol.ymma

import io.ktor.server.application.Application
import io.ktor.server.netty.EngineMain
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import org.andreasmlbngaol.ymma.auth.authRoute

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    contentNegotiationPlugin()
    authPlugin()

    routing {
        route("/api") {
            get { call.respondText("Allo Woldeu") }
            authRoute()
        }
    }
}