package ru.students.services

import org.ktorm.dsl.eq
import org.ktorm.entity.*
import ru.students.models.Chair
import ru.students.models.ChairRequest
import ru.students.models.Chairs

class ChairService {
    private val connection = DatabaseService.getConnection();

    fun create(request: ChairRequest): Boolean = connection.sequenceOf(Chairs)
        .add(Chair {
            NameChair = request.NameChair
            ShortNameChair = request.ShortNameChair
            Faculty = FacultyService().getById(request.IdFaculty)!!
        }) == 1

    fun list(): Set<Chair> =
        connection.sequenceOf(Chairs).toSet()

    fun getById(id: Int): Chair? =
        connection.sequenceOf(Chairs)
            .find { chair -> chair.Id eq id }
    fun getByFacultyId(id: Int): Set<Chair>? =
        connection.sequenceOf(Chairs)
            .filter { chair -> chair.Faculty eq id }.toSet()

    fun update(id: Int, request: ChairRequest): Boolean {
        val chair = getById(id)
        chair?.NameChair = request.NameChair
        chair?.ShortNameChair = request.ShortNameChair
        chair?.Faculty = FacultyService().getById(request.IdFaculty)!!

        return chair?.flushChanges() == 1
    }

    fun delete(id: Int): Boolean = getById(id)?.delete() == 1
}