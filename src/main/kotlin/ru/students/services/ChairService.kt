package ru.students.services

import org.ktorm.dsl.eq
import org.ktorm.entity.add
import org.ktorm.entity.find
import org.ktorm.entity.sequenceOf
import org.ktorm.entity.toSet
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

    fun update(id: Int, request: ChairRequest): Boolean {
        val chair = getById(id)
        chair?.NameChair = request.NameChair
        chair?.ShortNameChair = request.ShortNameChair
        chair?.Faculty = FacultyService().getById(request.IdFaculty)!!

        return chair?.flushChanges() == 1
    }

    fun delete(id: Int): Boolean = getById(id)?.delete() == 1
}