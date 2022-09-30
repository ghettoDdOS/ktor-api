package ru.students.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.students.models.*
import ru.students.services.ChoiceService

private fun Choice?.toResponse(): ChoiceResponse? =
    this?.let {
        ChoiceResponse(
            it.Id!!,
            it.Question.Id!!,
            it.User.Id!!,
            it.ChoiceUser
        )
    }

fun Application.configureChoiceRoutes() {
    routing {
        route("/choice") {
            val choiceService = ChoiceService()
            createRoute(choiceService)
            listRoute(choiceService)
            getByIdRoute(choiceService)
            getByUserIdRoute(choiceService)
            getByQuestionIdRoute(choiceService)
            updateRoute(choiceService)
            deleteRoute(choiceService)
        }
    }
}

fun Route.createRoute(service: ChoiceService) {
    post {
        if (service.create(call.receive<ChoiceRequest>()))
            call.respond(HttpStatusCode.Created)
        else
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Ошибка при создании результата голосования"))
    }
}

fun Route.listRoute(service: ChoiceService) {
    get {
        call.respond(
            message = service.list()
                .map(Choice::toResponse)
        )
    }
}

fun Route.getByIdRoute(service: ChoiceService) {
    get("/{id}") {
        val id: Int = call.parameters["id"]?.toIntOrNull()
            ?: return@get call.respond(HttpStatusCode.BadRequest, ErrorResponse("Неправильный Id"))

        service.getById(id)
            ?.let { choice -> choice.toResponse() }
            ?.let { response -> call.respond(response) }
            ?: return@get call.respond(HttpStatusCode.BadRequest, ErrorResponse("Результат голосования с Id:[$id] не найден"))
    }
}

fun Route.getByUserIdRoute(service: ChoiceService) {
    get("/user/{id}") {
        val id: Int = call.parameters["id"]?.toIntOrNull()
            ?: return@get call.respond(HttpStatusCode.BadRequest, ErrorResponse("Неправильный Id"))

        service.getByUserId(id)
            ?.let { choice -> choice.map(Choice::toResponse) }
            ?.let { response -> call.respond(response) }
            ?: return@get call.respond(HttpStatusCode.BadRequest, ErrorResponse("Результат голосования с Id Пользователя :[$id] не найдена"))
    }
}

fun Route.getByQuestionIdRoute(service: ChoiceService) {
    get("/question/{id}") {
        val id: Int = call.parameters["id"]?.toIntOrNull()
            ?: return@get call.respond(HttpStatusCode.BadRequest, ErrorResponse("Неправильный Id"))

        service.getByQuestionId(id)
            ?.let { choice -> choice.map(Choice::toResponse) }
            ?.let { response -> call.respond(response) }
            ?: return@get call.respond(HttpStatusCode.BadRequest, ErrorResponse("Результат голосования с Id Вопросов голосования :[$id] не найдена"))
    }
}

fun Route.updateRoute(service: ChoiceService) {
    patch("/{id}") {
        val id: Int = call.parameters["id"]?.toIntOrNull()
            ?: return@patch call.respond(HttpStatusCode.BadRequest, ErrorResponse("Неправильный Id"))

        if (service.update(id, call.receive<ChoiceRequest>()))
            call.respond(HttpStatusCode.NoContent)
        else
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Ошибка при обновлении результата голосования с Id:[$id]"))
    }
}

fun Route.deleteRoute(service: ChoiceService) {
    delete("/{id}") {
        val id: Int = call.parameters["id"]?.toIntOrNull()
            ?: return@delete call.respond(HttpStatusCode.BadRequest, ErrorResponse("Неправильный Id"))

        if (service.delete(id))
            call.respond(HttpStatusCode.NoContent)
        else
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Невозможно удалить результат голосования с таким Id:[$id]"))
    }
}