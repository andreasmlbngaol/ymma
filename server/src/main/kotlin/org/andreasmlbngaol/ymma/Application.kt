package org.andreasmlbngaol.ymma

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.andreasmlbngaol.ymma.controllers.auth.JwtConfig
import org.andreasmlbngaol.ymma.controllers.auth.authRoute
import org.andreasmlbngaol.ymma.controllers.comment.commentRoute
import org.andreasmlbngaol.ymma.controllers.course.courseRoute
import org.andreasmlbngaol.ymma.controllers.post.postRoute
import org.andreasmlbngaol.ymma.database.DatabaseFactory
import org.andreasmlbngaol.ymma.plugins.authenticationPlugin
import org.andreasmlbngaol.ymma.plugins.contentNegotiationPlugin
import org.andreasmlbngaol.ymma.plugins.statusPagesPlugin
import org.andreasmlbngaol.ymma.utils.respondJson

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    initKtor(environment.config)

    contentNegotiationPlugin()
    authenticationPlugin()
    statusPagesPlugin()

    routing {
        route("/") {
            get {
                call.respondRedirect("/api")
            }
        }
        route("/api") {
            get { call.respondJson(HttpStatusCode.OK, "Allo Woldeu") }

            authRoute()
            courseRoute()
            postRoute()
            commentRoute()
        }
    }
}

fun initKtor(
    config: ApplicationConfig
) {
    JwtConfig.init(config)
    DatabaseFactory.init(config)
}