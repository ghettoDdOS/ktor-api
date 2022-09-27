package ru.students.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.students.models.*
import ru.students.services.ChairService

private fun Chair?.toResponse(): ChairResponse? =
    this?.let {
        ChairResponse(
            it.Id!!,
            FacultyResponse(it.Faculty.Id, it.Faculty.NameFaculty, it.Faculty.ShortNameFaculty),
            it.NameChair,
            it.ShortNameChair
        )
    }

fun Application.configureChairRoutes() {
    routing {
        route("/chair") {
            val chairService = ChairService()
            createRoute(chairService)
            listRoute(chairService)
            getByIdRoute(chairService)
            getByFacyltyIdRoute(chairService)
            updateRoute(chairService)
            deleteRoute(chairService)
        }
    }
}

fun Route.createRoute(service: ChairService) {
    post {
        if (service.create(call.receive<ChairRequest>()))
            call.respond(HttpStatusCode.Created)
        else
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Ошибка при создании Кафедры"))
    }
}

fun Route.listRoute(service: ChairService) {
    get {
        call.respond(
            message = service.list()
                .map(Chair::toResponse)
        )
    }
}

fun Route.getByIdRoute(service: ChairService) {
    get("/{id}") {
        val id: Int = call.parameters["id"]?.toIntOrNull()
            ?: return@get call.respond(HttpStatusCode.BadRequest, ErrorResponse("Неправильный Id"))

        service.getById(id)
            ?.let { chair -> chair.toResponse() }
            ?.let { response -> call.respond(response) }
            ?: return@get call.respond(HttpStatusCode.BadRequest, ErrorResponse("Кафедра с Id:[$id] не найдена"))
    }
}

fun Route.getByFacyltyIdRoute(service: ChairService) {
    get("/faculty/{id}") {
        val id: Int = call.parameters["id"]?.toIntOrNull()
            ?: return@get call.respond(HttpStatusCode.BadRequest, ErrorResponse("Неправильный Id"))

        service.getByFacultyId(id)
            ?.let { chair -> chair.map(Chair::toResponse) }
            ?.let { response -> call.respond(response) }
            ?: return@get call.respond(HttpStatusCode.BadRequest, ErrorResponse("Кафедра с Id Факультета :[$id] не найдена"))
    }
}

fun Route.updateRoute(service: ChairService) {
    patch("/{id}") {
        val id: Int = call.parameters["id"]?.toIntOrNull()
            ?: return@patch call.respond(HttpStatusCode.BadRequest, ErrorResponse("Неправильный Id"))

        if (service.update(id, call.receive<ChairRequest>()))
            call.respond(HttpStatusCode.NoContent)
        else
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Ошибка при обновлении кафедры с Id:[$id]"))
    }
}

fun Route.deleteRoute(service: ChairService) {
    delete("/{id}") {
        val id: Int = call.parameters["id"]?.toIntOrNull()
            ?: return@delete call.respond(HttpStatusCode.BadRequest, ErrorResponse("Неправильный Id"))

        if (service.delete(id))
            call.respond(HttpStatusCode.NoContent)
        else
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Невозможно удалить кафедру с таким Id:[$id]"))
    }
}