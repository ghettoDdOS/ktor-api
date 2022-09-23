package ru.students.models

import kotlinx.serialization.Serializable
import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

@Serializable
data class ChairRequest(
    val IdFaculty: Int,
    val NameChair: String,
    val ShortNameChair: String
)

@Serializable
data class ChairResponse(
    val Id: Int?,
    val Faculty: FacultyResponse,
    val NameChair: String,
    val ShortNameChair: String
)

interface Chair : Entity<Chair> {
    companion object : Entity.Factory<Chair>()

    val Id: Int?
    var Faculty: Faculty
    var NameChair: String
    var ShortNameChair: String
}

object Chairs : Table<Chair>("Chair") {
    val Id = int("Id").primaryKey().bindTo(Chair::Id)
    val Faculty = int("IdFaculty").references(Faculties) { it.Faculty }
    val NameChair = varchar("NameChair").bindTo(Chair::NameChair)
    val ShortNameChair = varchar("ShortNameChair").bindTo(Chair::ShortNameChair)

}