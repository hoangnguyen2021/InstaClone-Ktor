package hoang.myapp.routes

import hoang.myapp.data.comment.CommentDataSource
import hoang.myapp.data.user.UserDataSource
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

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