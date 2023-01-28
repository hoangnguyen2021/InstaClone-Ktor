package hoang.myapp.routes

import hoang.myapp.data.post.InstaClonePost
import hoang.myapp.data.post.PostDataSource
import hoang.myapp.data.requests.PostRequest
import hoang.myapp.data.user.UserDataSource
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.createPost(
    postDataSource: PostDataSource,
    userDataSource: UserDataSource
) {
    post("create") {
        val request = call.receiveNullable<PostRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val author = userDataSource.getUserByUsername(request.authorUsername)
        if (author == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        val instaClonePost = InstaClonePost(
            authorId = author._id,
            caption = request.caption,
            createdAt = request.createdAt,
            lastEditedAt = request.lastEditedAt,
            mediaPaths = request.mediaPaths
        )
        val wasAcknowledged = postDataSource.insertPost(instaClonePost)
        if (!wasAcknowledged) {
            call.respond(HttpStatusCode.InternalServerError, "Failed to create post")
            return@post
        }

        call.respond(HttpStatusCode.OK, "Post created successfully")
    }
}