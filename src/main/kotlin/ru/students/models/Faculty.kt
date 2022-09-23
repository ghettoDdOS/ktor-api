package ru.students.models

import kotlinx.serialization.Serializable
import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

@Serializable
data class FacultyRequest(
    val NameFaculty: String,
    val ShortNameFaculty: String
)

@Serializable
data class FacultyResponse(
    val Id: Int?,
    val NameFaculty: String,
    val ShortNameFaculty: String
)

interface Faculty : Entity<Faculty> {
    companion object : Entity.Factory<Faculty>()

    var Id: Int?
    var NameFaculty: String
    var ShortNameFaculty: String
}

object Faculties : Table<Faculty>("Faculty") {
    val Id = int("Id").primaryKey().bindTo(Faculty::Id)
    val NameFaculty = varchar("NameFaculty").bindTo(Faculty::NameFaculty)
    val ShortNameFaculty = varchar("ShortNameFaculty").bindTo(Faculty::ShortNameFaculty)
}