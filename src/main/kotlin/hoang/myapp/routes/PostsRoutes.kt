package hoang.myapp.routes

import hoang.myapp.data.comment.CommentDataSource
import hoang.myapp.data.post.*
import hoang.myapp.data.user.InstaCloneUser2
import hoang.myapp.data.user.UserDataSource
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.litote.kmongo.newId

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
        val canCreatePost = postDataSource.insertPost(instaClonePost)
        if (!canCreatePost) {
            call.respond(HttpStatusCode.InternalServerError, "Failed to create post")
            return@post
        }

        call.respond(HttpStatusCode.OK, "Post created successfully")
    }
}

fun Route.getPostsByUserId(
    postDataSource: PostDataSource,
    userDataSource: UserDataSource,
    commentDataSource: CommentDataSource
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

        call.respond(
            HttpStatusCode.OK,
            posts.map { post ->
                post.mapToInstaClonePost2(commentDataSource.findCommentsByIds(post.comments))
            }
        )
    }
}

fun Route.getPostById(
    postDataSource: PostDataSource,
    commentDataSource: CommentDataSource
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
        val comments = commentDataSource.findCommentsByIds(post.comments)

        call.respond(HttpStatusCode.OK, post.mapToInstaClonePost2(comments))
    }
}

fun Route.getCommentorsByPostId(
    postDataSource: PostDataSource,
    userDataSource: UserDataSource,
    commentDataSource: CommentDataSource
) {
    get("commentors-by-post-id") {
        val postId = call.request.queryParameters["postId"]
        if (postId == null) {
            call.respond(HttpStatusCode.BadRequest, "Missing parameters")
            return@get
        }

        val post = postDataSource.getPostById(postId)
        if (post == null) {
            call.respond(HttpStatusCode.BadRequest, "Post not found with the given id")
            return@get
        }

        val commentorsIds = commentDataSource
            .findCommentsByIds(post.comments)
            .map { it.authorId }
        val commentors = userDataSource
            .getUsersByIds(commentorsIds)
            .map { user ->
                InstaCloneUser2(
                    _id = user._id,
                    mobileNumber = user.mobileNumber,
                    email = user.email,
                    fullName = user.fullName,
                    username = user.username,
                    birthday = user.birthday,
                    agreedToPolicy = user.agreedToPolicy,
                    profilePicPath = user.profilePicPath
                )
            }

        call.respond(HttpStatusCode.OK, commentors)
    }
}

fun Route.likePost(
    postDataSource: PostDataSource,
    userDataSource: UserDataSource
) {
    put("like") {
        val postId = call.request.queryParameters["postId"]
        val userId = call.request.queryParameters["userId"]
        if (postId == null || userId == null) {
            call.respond(HttpStatusCode.BadRequest, "Missing parameters")
            return@put
        }

        val user = userDataSource.getUserById(userId)
        if (user == null) {
            call.respond(HttpStatusCode.BadRequest, "User not found with the given id")
            return@put
        }

        val wasAcknowledge = postDataSource.likePost(postId, user._id)
        if (!wasAcknowledge) {
            call.respond(HttpStatusCode.InternalServerError, "Failed to like post")
            return@put
        }

        call.respond(HttpStatusCode.OK, "Post liked successfully")
    }
}

fun Route.unlikePost(
    postDataSource: PostDataSource,
    userDataSource: UserDataSource
) {
    put("unlike") {
        val postId = call.request.queryParameters["postId"]
        val userId = call.request.queryParameters["userId"]
        if (postId == null || userId == null) {
            call.respond(HttpStatusCode.BadRequest, "Missing parameters")
            return@put
        }

        val user = userDataSource.getUserById(userId)
        if (user == null) {
            call.respond(HttpStatusCode.BadRequest, "User not found with the given id")
            return@put
        }

        val wasAcknowledge = postDataSource.unlikePost(postId, user._id)
        if (!wasAcknowledge) {
            call.respond(HttpStatusCode.InternalServerError, "Failed to unlike post")
            return@put
        }

        call.respond(HttpStatusCode.OK, "Post unliked successfully")
    }
}

fun Route.commentOnPost(
    postDataSource: PostDataSource,
    userDataSource: UserDataSource,
    commentDataSource: CommentDataSource
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
        val commentId = newId<Comment>()
        val comment = Comment(
            _id = commentId,
            authorId = author._id,
            content = request.content,
            isEdited = request.isEdited,
            createdAt = request.createdAt,
            lastEditedAt = request.lastEditedAt,
            likes = request.likes,
            tags = request.tags
        )
        val canCreateComment = commentDataSource.insertComment(comment)
        if (!canCreateComment) {
            call.respond(HttpStatusCode.InternalServerError, "Failed to create comment")
            return@post
        }
        val canCommentOnPost = postDataSource.commentOnPost(request.postId, commentId.toString())
        if (!canCommentOnPost) {
            call.respond(HttpStatusCode.InternalServerError, "Failed to comment on post")
            return@post
        }

        call.respond(HttpStatusCode.OK, "Comment on post successfully")
    }
}

