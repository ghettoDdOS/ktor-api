package ru.students.services

import org.ktorm.dsl.eq
import org.ktorm.entity.add
import org.ktorm.entity.find
import org.ktorm.entity.sequenceOf
import org.ktorm.entity.toSet
import ru.students.models.Post
import ru.students.models.PostRequest
import ru.students.models.Posts

class PostService {
    private val connection = DatabaseService.getConnection();

    fun create(request: PostRequest): Boolean = connection.sequenceOf(Posts)
        .add(Post {
            NamePost = request.NamePost
        }) == 1

    fun list(): Set<Post> =
        connection.sequenceOf(Posts).toSet()

    fun getById(id: Int): Post? =
        connection.sequenceOf(Posts)
            .find { post -> post.Id eq id }

    fun update(id: Int, request: PostRequest): Boolean {
        val post = getById(id)
        post?.NamePost = request.NamePost

        return post?.flushChanges() == 1
    }

    fun delete(id: Int): Boolean = getById(id)?.delete() == 1

}