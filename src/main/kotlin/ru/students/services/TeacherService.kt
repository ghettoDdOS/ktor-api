package ru.students.services

import org.ktorm.dsl.eq
import org.ktorm.entity.*
import ru.students.models.Teacher
import ru.students.models.TeacherRequest
import ru.students.models.Teachers

class TeacherService {
    private val connection = DatabaseService.getConnection();

    fun create(request: TeacherRequest): Boolean = connection.sequenceOf(Teachers)
        .add(Teacher {
            Chair = ChairService().getById(request.Chair)!!
            Post = PostService().getById(request.Post)!!
            FirstName = request.FirstName
            SecondName = request.SecondName
            LastName = request.LastName
            Phone = request.Phone
            Email = request.Email
        }) == 1

    fun list(): Set<Teacher> =
        connection.sequenceOf(Teachers).toSet()

    fun getById(id: Int): Teacher? =
        connection.sequenceOf(Teachers)
            .find { teacher -> teacher.Id eq id }

    fun getByChairId(id: Int): Set<Teacher> =
        connection.sequenceOf(Teachers)
            .filter { teacher -> teacher.IdChair eq id }.toSet()

    fun update(id: Int, request: TeacherRequest): Boolean {
        val teacher = getById(id)
        teacher?.Chair = ChairService().getById(request.Chair)!!
        teacher?.FirstName = request.FirstName
        teacher?.Post = PostService().getById(request.Post)!!
        teacher?.SecondName = request.SecondName
        teacher?.LastName = request.LastName
        teacher?.Phone = request.Phone
        teacher?.Email = request.Email
        return teacher?.flushChanges() == 1
    }

    fun delete(id: Int): Boolean = getById(id)?.delete() == 1
}