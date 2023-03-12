package hoang.myapp.routes

import hoang.myapp.data.comments.ReplyCommentDataSource
import hoang.myapp.data.user.UserDataSource
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.likeReplyComment(
    replyCommentDataSource: ReplyCommentDataSource,
    userDataSource: UserDataSource
) {
    put("like") {
        val replyCommentId = call.request.queryParameters["replyCommentId"]
        val userId = call.request.queryParameters["userId"]
        if (replyCommentId == null || userId == null) {
            call.respond(HttpStatusCode.BadRequest, "Missing parameters")
            return@put
        }

        val user = userDataSource.findUserById(userId)
        if (user == null) {
            call.respond(HttpStatusCode.BadRequest, "User not found with the given id")
            return@put
        }

        val canLikeReplyComment = replyCommentDataSource.likeReplyComment(replyCommentId, user._id)
        if (!canLikeReplyComment) {
            call.respond(HttpStatusCode.InternalServerError, "Failed to like reply comment")
            return@put
        }

        call.respond(HttpStatusCode.OK, "Reply comment liked successfully")
    }
}

fun Route.unlikeReplyComment(
    replyCommentDataSource: ReplyCommentDataSource,
    userDataSource: UserDataSource
) {
    put("unlike") {
        val replyCommentId = call.request.queryParameters["replyCommentId"]
        val userId = call.request.queryParameters["userId"]
        if (replyCommentId == null || userId == null) {
            call.respond(HttpStatusCode.BadRequest, "Missing parameters")
            return@put
        }

        val user = userDataSource.findUserById(userId)
        if (user == null) {
            call.respond(HttpStatusCode.BadRequest, "User not found with the given id")
            return@put
        }

        val canUnlikeReplyComment = replyCommentDataSource.unlikeReplyComment(replyCommentId, user._id)
        if (!canUnlikeReplyComment) {
            call.respond(HttpStatusCode.InternalServerError, "Failed to unlike reply comment")
            return@put
        }

        call.respond(HttpStatusCode.OK, "Reply comment unliked successfully")
    }
}