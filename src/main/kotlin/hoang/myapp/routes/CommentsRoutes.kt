package hoang.myapp.routes

import hoang.myapp.data.comments.*
import hoang.myapp.data.user.UserDataSource
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.litote.kmongo.newId

fun Route.likeComment(
    commentDataSource: CommentDataSource,
    userDataSource: UserDataSource
) {
    put("like") {
        val commentId = call.request.queryParameters["commentId"]
        val userId = call.request.queryParameters["userId"]
        if (commentId == null || userId == null) {
            call.respond(HttpStatusCode.BadRequest, "Missing parameters")
            return@put
        }

        val user = userDataSource.getUserById(userId)
        if (user == null) {
            call.respond(HttpStatusCode.BadRequest, "User not found with the given id")
            return@put
        }

        val wasAcknowledge = commentDataSource.likeComment(commentId, user._id)
        if (!wasAcknowledge) {
            call.respond(HttpStatusCode.InternalServerError, "Failed to like comment")
            return@put
        }

        call.respond(HttpStatusCode.OK, "Comment liked successfully")
    }
}

fun Route.unlikeComment(
    commentDataSource: CommentDataSource,
    userDataSource: UserDataSource
) {
    put("unlike") {
        val commentId = call.request.queryParameters["commentId"]
        val userId = call.request.queryParameters["userId"]
        if (commentId == null || userId == null) {
            call.respond(HttpStatusCode.BadRequest, "Missing parameters")
            return@put
        }

        val user = userDataSource.getUserById(userId)
        if (user == null) {
            call.respond(HttpStatusCode.BadRequest, "User not found with the given id")
            return@put
        }

        val wasAcknowledge = commentDataSource.unlikeComment(commentId, user._id)
        if (!wasAcknowledge) {
            call.respond(HttpStatusCode.InternalServerError, "Failed to unlike comment")
            return@put
        }

        call.respond(HttpStatusCode.OK, "Comment unliked successfully")
    }
}

fun Route.replyToComment(
    commentDataSource: CommentDataSource,
    userDataSource: UserDataSource,
    replyCommentDataSource: ReplyCommentDataSource
) {
    post("reply") {
        val request = call.receiveNullable<ReplyCommentRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val author = userDataSource.getUserById(request.authorId)
        if (author == null) {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        val replyCommentId = newId<ReplyComment>()
        val replyComment = ReplyComment(
            _id = replyCommentId,
            authorId = author._id,
            content = request.content,
            isEdited = request.isEdited,
            createdAt = request.createdAt,
            lastEditedAt = request.lastEditedAt,
            tags = request.tags
        )
        val canCreateReplyComment = replyCommentDataSource.insertReplyComment(replyComment)
        if (!canCreateReplyComment) {
            call.respond(HttpStatusCode.InternalServerError, "Failed to create reply comment")
            return@post
        }
        val canReplyToComment = commentDataSource.replyToComment(request.commentId, replyCommentId.toString())
        if (!canReplyToComment) {
            call.respond(HttpStatusCode.InternalServerError, "Failed to reply to comment")
            return@post
        }

        call.respond(HttpStatusCode.OK, "Reply to comment successfully")
    }
}