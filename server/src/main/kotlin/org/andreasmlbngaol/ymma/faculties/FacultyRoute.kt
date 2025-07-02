package org.andreasmlbngaol.ymma.faculties

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.andreasmlbngaol.ymma.database.dao.CollegesDao
import org.andreasmlbngaol.ymma.database.dao.FacultiesDao
import org.andreasmlbngaol.ymma.domains.faculty.CreateFacultyRequest
import org.andreasmlbngaol.ymma.domains.faculty.CreateFacultyResponse
import org.andreasmlbngaol.ymma.utils.respondJson

fun Route.facultyRoute() {
    authenticate("auth") {
        route("/colleges/{collegeId}/faculties") {
            post {
                val collegeId = call.parameters["collegeId"]?.toLongOrNull()
                    ?: return@post call.respondJson(HttpStatusCode.BadRequest, "Invalid collegeId")

                if(!CollegesDao.existById(collegeId)) return@post call.respondJson(HttpStatusCode.NotFound, "College not found")

                val payload = call.receive<CreateFacultyRequest>()

                val name = payload.name.trim()
                val abbreviation = payload.abbreviation?.trim()?.uppercase()

                if(name.isEmpty()) return@post call.respondJson(HttpStatusCode.BadRequest, "Name must not be empty")
                if(abbreviation != null && abbreviation.length > 20) {
                    return@post call.respondJson(
                        HttpStatusCode.BadRequest,
                        "Abbreviation must not exceed 20 characters"
                    )
                }

                val facultyId = FacultiesDao
                    .create(
                        collegeId = collegeId,
                        name = name,
                        abbreviation = abbreviation
                    )

                call.respond(
                    HttpStatusCode.Created,
                    CreateFacultyResponse(
                        id = facultyId.value,
                        name = name,
                        abbreviation = abbreviation
                    )
                )
            }

            get {
                val collegeId = call.parameters["collegeId"]?.toLongOrNull()
                    ?: return@get call.respondJson(HttpStatusCode.BadRequest, "Invalid collegeId")

                if(!CollegesDao.existById(collegeId)) return@get call.respondJson(HttpStatusCode.NotFound, "College not found")

                val faculties = FacultiesDao
                    .findAllByCollegeId(collegeId)

                call.respond(faculties)
            }
        }
    }
}