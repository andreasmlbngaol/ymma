package org.andreasmlbngaol.ymma.colleges

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.andreasmlbngaol.ymma.domains.college.CreateCollegeRequest
import org.andreasmlbngaol.ymma.domains.college.CreateCollegeResponse
import org.andreasmlbngaol.ymma.database.dao.CollegesDao
import org.andreasmlbngaol.ymma.utils.respondJson

fun Route.collegeRoute() {
    authenticate("auth") {
        route("/colleges") {
            post {
                val payload = call.receive<CreateCollegeRequest>()

                val name = payload.name.trim()
                val abbreviation = payload.abbreviation?.trim()?.uppercase()

                if (name.isEmpty()) return@post call.respondJson(HttpStatusCode.BadRequest, "Name must not be empty")
                if (abbreviation != null && abbreviation.length > 10) {
                    return@post call.respondJson(
                        HttpStatusCode.BadRequest,
                        "Abbreviation must not exceed 10 characters"
                    )
                }

                val collegeId = CollegesDao.create(
                    name = name,
                    abbreviation = abbreviation
                )

                call.respond(
                    HttpStatusCode.Created,
                    CreateCollegeResponse(
                        id = collegeId.value,
                        name = name,
                        abbreviation = abbreviation
                    )
                )
            }
        }
    }
}
