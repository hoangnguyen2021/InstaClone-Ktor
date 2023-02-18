package hoang.myapp.routes

import hoang.myapp.data.user.InstaCloneUser2
import hoang.myapp.data.user.UserDataSource
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

        val user = userDataSource.getUserById(id)
        if (user == null) {
            call.respond(HttpStatusCode.BadRequest, "User not found with the given id")
            return@get
        }

        call.respond(
            HttpStatusCode.OK,
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
        )
    }
}