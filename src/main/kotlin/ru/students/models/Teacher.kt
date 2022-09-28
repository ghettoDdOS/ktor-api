package ru.students.models

import kotlinx.serialization.Serializable
import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

@Serializable
data class TeacherRequest(
    val Chair: Int,
    val Post: Int,
    val FirstName: String,
    val SecondName: String,
    val LastName: String,
    val Phone: String,
    val Email: String
)

@Serializable
data class TeacherResponse(
    val Id: Int?,
    val Chair: Int,
    val Post: PostResponse,
    val FirstName: String,
    val SecondName: String,
    val LastName: String,
    val Phone: String,
    val Email: String
)

interface Teacher : Entity<Teacher> {
    companion object : Entity.Factory<Teacher>()

    var Id: Int?
    var Chair: Chair
    var Post: Post
    var FirstName: String
    var SecondName: String
    var LastName: String
    var Phone: String
    var Email: String
}

object Teachers : Table<Teacher>("Teacher") {
    val Id = int("Id").primaryKey().bindTo(Teacher::Id)
    val IdChair = int("IdChair").references(Chairs) { it.Chair }
    val IdPost = int("IdPost").references(Posts) { it.Post }
    val FirstName = varchar("FirstName").bindTo(Teacher::FirstName)
    val SecondName = varchar("SecondName").bindTo(Teacher::SecondName)
    val LastName = varchar("LastName").bindTo(Teacher::LastName)
    val Phone = varchar("Phone").bindTo(Teacher::Phone)
    val Email = varchar("Email").bindTo(Teacher::Email)

}