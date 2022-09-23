package ru.students

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import ru.students.plugins.configureSerialization
import ru.students.routes.configureChairRoutes
import ru.students.routes.configureFacultyRoutes
import ru.students.routes.configurePostRoutes
import ru.students.routes.configureTeacherRoutes

fun main() {
    embeddedServer(Netty, port = 8080, host = "localhost") {
        configureFacultyRoutes()
        configureChairRoutes()
        configurePostRoutes()
        configureTeacherRoutes()
        configureSerialization()
    }.start(wait = true)
}
