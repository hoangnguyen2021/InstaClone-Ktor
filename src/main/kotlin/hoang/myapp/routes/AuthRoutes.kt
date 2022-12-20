package hoang.myapp.routes

import hoang.myapp.data.requests.AuthRequest
import hoang.myapp.data.responses.AuthResponse
import hoang.myapp.data.user.User
import hoang.myapp.data.user.UserDataSource
import hoang.myapp.security.hashing.HashingService
import hoang.myapp.security.hashing.SaltedHash
import hoang.myapp.security.token.TokenClaim
import hoang.myapp.security.token.TokenConfig
import hoang.myapp.security.token.TokenService
import hoang.myapp.utils.Validator
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.signUp(
    hashingService: HashingService,
    userDataSource: UserDataSource
) {
    post("sign-up") {
        val request = call.receiveNullable<AuthRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        if (!Validator.validateUsername(request.username)) {
            call.respond(HttpStatusCode.BadRequest, "Invalid username")
            return@post
        }
        if (!Validator.validatePassword(request.password)) {
            call.respond(HttpStatusCode.BadRequest, "Invalid password")
            return@post
        }

        val saltedHash = hashingService.generateSaltedHash(request.password)
        val user = User(
            username = request.username,
            password = saltedHash.hash,
            salt = saltedHash.salt
        )
        val wasAcknowledged = userDataSource.insertUser(user)
        if (!wasAcknowledged) {
            call.respond(HttpStatusCode.Conflict)
            return@post
        }

        call.respond(HttpStatusCode.OK)
    }
}

fun Route.signIn(
    hashingService: HashingService,
    userDataSource: UserDataSource,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {
    post("sign-in") {
        val request = call.receiveNullable<AuthRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val user = userDataSource.getUserByUsername(request.username)
        if (user == null) {
            call.respond(HttpStatusCode.Conflict, "Cannot find user in database")
            return@post
        }

        val saltedHash = SaltedHash(user.password, user.salt)
        val isValidPassword = hashingService.verify(request.password, saltedHash)
        if (!isValidPassword) {
            call.respond(HttpStatusCode.Conflict, "Incorrect password")
            return@post
        }

        val tokenClaim = TokenClaim(name = "userId", value = user.id.toString())
        val token = tokenService.generate(tokenConfig, tokenClaim)

        call.respond(
            status = HttpStatusCode.OK,
            message = AuthResponse(
                token = token
            )
        )
    }
}

fun Route.authenticate() {
    authenticate {
        get("authenticate") {
            call.respond(HttpStatusCode.OK)
        }
    }
}

fun Route.getSecretInfo() {
    authenticate {
        get("secret") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", String::class)
            call.respond(HttpStatusCode.OK, "Your userId is $userId")
        }
    }
}