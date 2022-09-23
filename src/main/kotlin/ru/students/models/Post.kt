package ru.students.models

import kotlinx.serialization.Serializable
import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

@Serializable
data class PostRequest(
    val NamePost: String
)

@Serializable
data class PostResponse(
    val Id: Int?,
    val NamePost: String
)

interface Post : Entity<Post> {
    companion object : Entity.Factory<Post>()

    var Id: Int?
    var NamePost: String
}

object Posts : Table<Post>("Post") {
    val Id = int("Id").primaryKey().bindTo(Post::Id)
    val NamePost = varchar("NamePost").bindTo(Post::NamePost)

}