package ru.students.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.students.models.ErrorResponse
import ru.students.models.Post
import ru.students.models.PostRequest
import ru.students.models.PostResponse
import ru.students.services.PostService

private fun Post?.toResponse(): PostResponse? =
    this?.let { PostResponse(it.Id!!, it.NamePost) }

fun Application.configurePostRoutes() {
    routing {
        route("/post") {
            val postService = PostService()
            createRoute(postService)
            listRoute(postService)
            getByIdRoute(postService)
            updateRoute(postService)
            deleteRoute(postService)
        }
    }
}

fun Route.createRoute(service: PostService) {
    post {
        if (service.create(call.receive<PostRequest>()))
            call.respond(HttpStatusCode.Created)
        else
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Ошибка при создании Должности"))
    }
}

fun Route.listRoute(service: PostService) {
    get {
        call.respond(
            message = service.list()
                .map(Post::toResponse)
        )
    }
}

fun Route.getByIdRoute(service: PostService) {
    get("/{id}") {
        val id: Int = call.parameters["id"]?.toIntOrNull()
            ?: return@get call.respond(HttpStatusCode.BadRequest, ErrorResponse("Неправильный Id"))

        service.getById(id)
            ?.let { post -> post.toResponse() }
            ?.let { response -> call.respond(response) }
            ?: return@get call.respond(HttpStatusCode.BadRequest, ErrorResponse("Должность с Id:[$id] не найдена"))
    }
}

fun Route.updateRoute(service: PostService) {
    patch("/{id}") {
        val id: Int = call.parameters["id"]?.toIntOrNull()
            ?: return@patch call.respond(HttpStatusCode.BadRequest, ErrorResponse("Неправильный Id"))

        if (service.update(id, call.receive<PostRequest>()))
            call.respond(HttpStatusCode.NoContent)
        else
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Ошибка при обновлении должности с Id:[$id]"))
    }
}

fun Route.deleteRoute(service: PostService) {
    delete("/{id}") {
        val id: Int = call.parameters["id"]?.toIntOrNull()
            ?: return@delete call.respond(HttpStatusCode.BadRequest, ErrorResponse("Неправильный Id"))

        if (service.delete(id))
            call.respond(HttpStatusCode.NoContent)
        else
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Невозможно удалить должность с таким Id:[$id]"))
    }
}