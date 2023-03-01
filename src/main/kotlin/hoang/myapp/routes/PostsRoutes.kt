package hoang.myapp.routes

import hoang.myapp.data.post.*
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

fun Route.getPostsByUserId(
    postDataSource: PostDataSource,
    userDataSource: UserDataSource
) {
    get("posts-by-user-id") {
        val userId = call.request.queryParameters["userId"]
        if (userId == null) {
            call.respond(HttpStatusCode.BadRequest, "Missing parameters")
            return@get
        }

        val author = userDataSource.getUserById(userId)
        if (author == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }

        val posts = postDataSource.getPostsByUser(author._id)
        call.respond(HttpStatusCode.OK, posts)
    }
}

fun Route.getPostById(
    postDataSource: PostDataSource
) {
    get("post-by-id") {
        val id = call.request.queryParameters["id"]
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest, "Missing parameters")
            return@get
        }

        val post = postDataSource.getPostById(id)
        if (post == null) {
            call.respond(HttpStatusCode.BadRequest, "Post not found with the given id")
            return@get
        }

        call.respond(HttpStatusCode.OK, post)
    }
}

fun Route.likePost(
    postDataSource: PostDataSource,
    userDataSource: UserDataSource
) {
    get("like") {
        val postId = call.request.queryParameters["postId"]
        val userId = call.request.queryParameters["userId"]
        if (postId == null || userId == null) {
            call.respond(HttpStatusCode.BadRequest, "Missing parameters")
            return@get
        }

        val user = userDataSource.getUserById(userId)
        if (user == null) {
            call.respond(HttpStatusCode.BadRequest, "User not found with the given id")
            return@get
        }

        val wasAcknowledge = postDataSource.likePost(postId, user._id)
        if (!wasAcknowledge) {
            call.respond(HttpStatusCode.InternalServerError, "Failed to like post")
            return@get
        }

        call.respond(HttpStatusCode.OK, "Post liked successfully")
    }
}

fun Route.unlikePost(
    postDataSource: PostDataSource,
    userDataSource: UserDataSource
) {
    get("unlike") {
        val postId = call.request.queryParameters["postId"]
        val userId = call.request.queryParameters["userId"]
        if (postId == null || userId == null) {
            call.respond(HttpStatusCode.BadRequest, "Missing parameters")
            return@get
        }

        val user = userDataSource.getUserById(userId)
        if (user == null) {
            call.respond(HttpStatusCode.BadRequest, "User not found with the given id")
            return@get
        }

        val wasAcknowledge = postDataSource.unlikePost(postId, user._id)
        if (!wasAcknowledge) {
            call.respond(HttpStatusCode.InternalServerError, "Failed to unlike post")
            return@get
        }

        call.respond(HttpStatusCode.OK, "Post unliked successfully")
    }
}

fun Route.commentOnPost(
    postDataSource: PostDataSource,
    userDataSource: UserDataSource
) {
    post("comment") {
        val request = call.receiveNullable<CommentRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val author = userDataSource.getUserById(request.authorId)
        if (author == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        val comment = Comment(
            authorId = author._id,
            content = request.content,
            isEdited = request.isEdited,
            createdAt = request.createdAt,
            lastEditedAt = request.lastEditedAt,
            likes = request.likes,
            tags = request.tags
        )
        val wasAcknowledged = postDataSource.commentOnPost(request.postId, comment)
        if (!wasAcknowledged) {
            call.respond(HttpStatusCode.InternalServerError, "Failed to create comment")
            return@post
        }

        call.respond(HttpStatusCode.OK, "Post created successfully")
    }
}