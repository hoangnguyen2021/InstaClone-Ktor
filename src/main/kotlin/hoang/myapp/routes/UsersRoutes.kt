package hoang.myapp.routes

import hoang.myapp.data.user.InstaCloneUser2
import hoang.myapp.data.user.UserDataSource
import hoang.myapp.data.user.toInstaCloneUser2
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getUserById(
    userDataSource: UserDataSource
) {
    get() {
        val id = call.request.queryParameters["id"]
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest, "Missing parameters")
            return@get
        }

        val user = userDataSource.findUserById(id)
        if (user == null) {
            call.respond(HttpStatusCode.BadRequest, "User not found with the given id")
            return@get
        }

        call.respond(HttpStatusCode.OK, user.toInstaCloneUser2())
    }
}