package ru.students.services

import org.ktorm.dsl.eq
import org.ktorm.entity.add
import org.ktorm.entity.find
import org.ktorm.entity.sequenceOf
import org.ktorm.entity.toSet
import ru.students.models.Faculties
import ru.students.models.Faculty
import ru.students.models.FacultyRequest

class FacultyService {
    private val connection = DatabaseService.getConnection();

    fun create(request: FacultyRequest): Boolean = connection.sequenceOf(Faculties)
        .add(Faculty {
            NameFaculty = request.NameFaculty
            ShortNameFaculty = request.ShortNameFaculty
        }) == 1

    fun list(): Set<Faculty> =
        connection.sequenceOf(Faculties).toSet()

    fun getById(id: Int): Faculty? =
        connection.sequenceOf(Faculties)
            .find { faculty -> faculty.Id eq id }

    fun update(id: Int, request: FacultyRequest): Boolean {
        val faculty = getById(id)
        faculty?.NameFaculty = request.NameFaculty
        faculty?.ShortNameFaculty = request.ShortNameFaculty

        return faculty?.flushChanges() == 1
    }

    fun delete(id: Int): Boolean = getById(id)?.delete() == 1
}