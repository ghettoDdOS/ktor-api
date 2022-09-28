package ru.students.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.students.models.*
import ru.students.services.TeacherService

private fun Teacher?.toResponse(): TeacherResponse? =
    this?.let {
        TeacherResponse(
            it.Id!!,
            it.Chair.Id!!,
            PostResponse(it.Post.Id, it.Post.NamePost),
            it.FirstName,
            it.SecondName,
            it.LastName,
            it.Phone,
            it.Email
        )
    }

fun Application.configureTeacherRoutes() {
    routing {
        route("/teacher") {
            val teacherService = TeacherService()
            createRoute(teacherService)
            listRoute(teacherService)
            getByIdRoute(teacherService)
            getByChairIdRoute(teacherService)
            updateRoute(teacherService)
            deleteRoute(teacherService)
        }
    }
}

fun Route.createRoute(service: TeacherService) {
    post {
        if (service.create(call.receive<TeacherRequest>()))
            call.respond(HttpStatusCode.Created)
        else
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Ошибка при создании Преподавателя"))
    }
}

fun Route.listRoute(service: TeacherService) {
    get {
        call.respond(
            message = service.list()
                .map(Teacher::toResponse)
        )
    }
}

fun Route.getByIdRoute(service: TeacherService) {
    get("/{id}") {
        val id: Int = call.parameters["id"]?.toIntOrNull()
            ?: return@get call.respond(HttpStatusCode.BadRequest, ErrorResponse("Неправильный Id"))

        service.getById(id)
            ?.let { teacher -> teacher.toResponse() }
            ?.let { response -> call.respond(response) }
            ?: return@get call.respond(HttpStatusCode.BadRequest, ErrorResponse("Преподаватель с Id:[$id] не найден"))
    }
}

fun Route.getByChairIdRoute(service: TeacherService) {
    get("/chair/{id}") {
        val id: Int = call.parameters["id"]?.toIntOrNull()
            ?: return@get call.respond(HttpStatusCode.BadRequest, ErrorResponse("Неправильный Id"))

        service.getByChairId(id)
            ?.let { teacher -> teacher.map(Teacher::toResponse) }
            ?.let { response -> call.respond(response) }
            ?: return@get call.respond(HttpStatusCode.BadRequest, ErrorResponse("Преподаватель с Id кафедры:[$id] не найден"))
    }
}

fun Route.updateRoute(service: TeacherService) {
    patch("/{id}") {
        val id: Int = call.parameters["id"]?.toIntOrNull()
            ?: return@patch call.respond(HttpStatusCode.BadRequest, ErrorResponse("Неправильный Id"))

        if (service.update(id, call.receive<TeacherRequest>()))
            call.respond(HttpStatusCode.NoContent)
        else
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Ошибка при обновлении преподавателя с Id:[$id]"))
    }
}

fun Route.deleteRoute(service: TeacherService) {
    delete("/{id}") {
        val id: Int = call.parameters["id"]?.toIntOrNull()
            ?: return@delete call.respond(HttpStatusCode.BadRequest, ErrorResponse("Неправильный Id"))

        if (service.delete(id))
            call.respond(HttpStatusCode.NoContent)
        else
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Невозможно удалить преподавателя с таким Id:[$id]"))
    }
}