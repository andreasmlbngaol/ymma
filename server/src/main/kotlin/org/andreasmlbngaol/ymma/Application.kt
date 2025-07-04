package org.andreasmlbngaol.ymma

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.netty.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.andreasmlbngaol.ymma.auth.JwtConfig
import org.andreasmlbngaol.ymma.auth.authRoute
import org.andreasmlbngaol.ymma.course.courseRoute
import org.andreasmlbngaol.ymma.database.DatabaseFactory
import org.andreasmlbngaol.ymma.plugins.authenticationPlugin
import org.andreasmlbngaol.ymma.plugins.contentNegotiationPlugin
import org.andreasmlbngaol.ymma.plugins.statusPagesPlugin
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
            courseRoute()

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