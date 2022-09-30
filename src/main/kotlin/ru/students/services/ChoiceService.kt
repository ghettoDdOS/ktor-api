package ru.students.services

import org.ktorm.dsl.eq
import org.ktorm.entity.*
import ru.students.models.Choice
import ru.students.models.ChoiceRequest
import ru.students.models.Choices

class ChoiceService {
    private val connection = DatabaseService.getConnection();

    fun create(request: ChoiceRequest): Boolean = connection.sequenceOf(Choices)
        .add(Choice {
            Question = QuestionService().getById(request.Question)!!
            User = UserService().getById(request.User)!!
            ChoiceUser = request.ChoiceUser
        }) == 1

    fun list(): Set<Choice> =
        connection.sequenceOf(Choices).toSet()

    fun getById(id: Int): Choice? =
        connection.sequenceOf(Choices)
            .find { choice -> choice.Id eq id }

    fun getByQuestionId(id: Int): Set<Choice>? =
        connection.sequenceOf(Choices)
            .filter { choice -> choice.Question eq id }.toSet()

    fun getByUserId(id: Int): Set<Choice>? =
        connection.sequenceOf(Choices)
            .filter { choice -> choice.User eq id }.toSet()

    fun update(id: Int, request: ChoiceRequest): Boolean {
        val choice = getById(id)
        choice?.Question = QuestionService().getById(request.Question)!!
        choice?.User = UserService().getById(request.User)!!
        choice?.ChoiceUser = request.ChoiceUser
        return choice?.flushChanges() == 1
    }

    fun delete(id: Int): Boolean = getById(id)?.delete() == 1
}