package ru.students.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.students.models.ErrorResponse
import ru.students.models.Faculty
import ru.students.models.FacultyRequest
import ru.students.models.FacultyResponse
import ru.students.services.FacultyService

private fun Faculty?.toResponse(): FacultyResponse? =
    this?.let { FacultyResponse(it.Id!!, it.NameFaculty, it.ShortNameFaculty) }

fun Application.configureFacultyRoutes() {
    routing {
        route("/faculty") {
            val facultyService = FacultyService()
            createRoute(facultyService)
            listRoute(facultyService)
            getByIdRoute(facultyService)
            updateRoute(facultyService)
            deleteRoute(facultyService)
        }
    }
}

fun Route.createRoute(service: FacultyService) {
    post {
        if (service.create(call.receive<FacultyRequest>()))
            call.respond(HttpStatusCode.Created)
        else
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Ошибка при создании Факультета"))
    }
}

fun Route.listRoute(service: FacultyService) {
    get {
        call.respond(
            message = service.list()
                .map(Faculty::toResponse)
        )
    }
}

fun Route.getByIdRoute(service: FacultyService) {
    get("/{id}") {
        val id: Int = call.parameters["id"]?.toIntOrNull()
            ?: return@get call.respond(HttpStatusCode.BadRequest, ErrorResponse("Неправильный Id"))

        service.getById(id)
            ?.let { faculty -> faculty.toResponse() }
            ?.let { response -> call.respond(response) }
            ?: return@get call.respond(HttpStatusCode.BadRequest, ErrorResponse("Факультет с Id:[$id] не найден"))
    }
}

fun Route.updateRoute(service: FacultyService) {
    patch("/{id}") {
        val id: Int = call.parameters["id"]?.toIntOrNull()
            ?: return@patch call.respond(HttpStatusCode.BadRequest, ErrorResponse("Неправильный Id"))

        if (service.update(id, call.receive<FacultyRequest>()))
            call.respond(HttpStatusCode.NoContent)
        else
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Ошибка при обновлении факультета с Id:[$id]"))
    }
}

fun Route.deleteRoute(service: FacultyService) {
    delete("/{id}") {
        val id: Int = call.parameters["id"]?.toIntOrNull()
            ?: return@delete call.respond(HttpStatusCode.BadRequest, ErrorResponse("Неправильный Id"))

        if (service.delete(id))
            call.respond(HttpStatusCode.NoContent)
        else
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Невозможно удалить факультет с таким Id:[$id]"))
    }
}