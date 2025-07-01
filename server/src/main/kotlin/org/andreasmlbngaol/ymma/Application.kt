package org.andreasmlbngaol.ymma

import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.server.application.Application
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.netty.EngineMain
import io.ktor.server.request.receiveMultipart
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.andreasmlbngaol.ymma.auth.JwtConfig
import org.andreasmlbngaol.ymma.auth.authRoute
import org.andreasmlbngaol.ymma.database.DatabaseFactory
import org.andreasmlbngaol.ymma.utils.respondJson
import java.io.File

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    initKtor(environment.config)

    contentNegotiationPlugin()
    authenticationPlugin()
    statusPagesPlugin()

    routing {
        route("/api") {
            get { call.respondJson(HttpStatusCode.OK, "Allo Woldeu") }
            authRoute()

            post("/upload") {
                withContext(Dispatchers.IO) {
                    val multipart = call.receiveMultipart()
                    var fileName: String? = null

                    multipart.forEachPart { part ->
                        when (part) {
                            is PartData.FileItem -> {
                                fileName = part.originalFileName
                                val fileBytes = part.streamProvider().readBytes()
                                File("uploads/$fileName").writeBytes(fileBytes)
                            }

                            else -> Unit
                        }
                        part.dispose()
                    }

                    if (fileName == null) call.respondJson(HttpStatusCode.BadRequest, "No file uploaded")
                    else call.respondJson(HttpStatusCode.OK, "File '$fileName' uploaded successfully")
                }
            }
        }
    }
}

fun initKtor(
    config: ApplicationConfig
) {
    JwtConfig.init(config)
    DatabaseFactory.init(config)
}