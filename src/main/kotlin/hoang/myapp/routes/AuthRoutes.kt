package hoang.myapp.routes

import hoang.myapp.data.requests.LoginRequest
import hoang.myapp.data.requests.SignupRequest
import hoang.myapp.data.responses.AuthResponse
import hoang.myapp.data.user.UserDataSource
import hoang.myapp.security.hashing.HashingService
import hoang.myapp.security.hashing.SaltedHash
import hoang.myapp.security.token.TokenClaim
import hoang.myapp.security.token.TokenConfig
import hoang.myapp.security.token.TokenService
import hoang.myapp.security.verification.VerificationService
import hoang.myapp.utils.Validator
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.sendVerificationCode(
    twilioVerificationService: VerificationService,
    sendinBlueTransactionalEmailService: VerificationService
) {
    get("send-verification-code") {
        val recipient = call.request.queryParameters["recipient"]
        if (recipient == null) {
            call.respond(HttpStatusCode.BadRequest, "Missing parameters")
            return@get
        }

        if (Validator.validateMobileNumber(recipient) || Validator.validateEmailAddress(recipient)) {
            val isSuccessful =
                if (Validator.validateMobileNumber(recipient)) {
                    twilioVerificationService.sendVerificationToken("+1${recipient}")
                } else {
                    sendinBlueTransactionalEmailService.sendVerificationToken(recipient)
                }
            if (isSuccessful) {
                call.respond(HttpStatusCode.OK, "Verification sent")
            } else {
                call.respond(HttpStatusCode.InternalServerError, "Failed to send verification")
            }
        } else {
            call.respond(HttpStatusCode.BadRequest, "Invalid parameters")
        }
    }
}

fun Route.checkVerificationCode(
    twilioVerificationService: VerificationService,
    sendinBlueTransactionalEmailService: VerificationService
) {
    get("check-verification-code") {
        val recipient = call.request.queryParameters["recipient"]
        val verificationCode = call.request.queryParameters["verification-code"]
        if (recipient == null || verificationCode == null) {
            call.respond(HttpStatusCode.BadRequest, "Missing parameters")
            return@get
        }

        if (Validator.validateMobileNumber(recipient) || Validator.validateEmailAddress(recipient)) {
            val isSuccessful =
                if (Validator.validateMobileNumber(recipient))
                    twilioVerificationService.checkVerificationToken("+1${recipient}", verificationCode)
                else
                    sendinBlueTransactionalEmailService.checkVerificationToken(recipient, verificationCode)
            if (isSuccessful) {
                call.respond(HttpStatusCode.OK, "Correct token")
            } else {
                call.respond(HttpStatusCode.BadRequest, "Failed to check verification")
            }
        } else {
            call.respond(HttpStatusCode.BadRequest, "Invalid parameters")
        }
    }
}

fun Route.signUp(
    hashingService: HashingService,
    userDataSource: UserDataSource
) {
    post("sign-up") {
        val request = call.receiveNullable<SignupRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val saltedHash = hashingService.generateSaltedHash(request.password)
        val instaCloneUser = request.mapToInstaCloneUser(saltedHash)
        val wasAcknowledged = userDataSource.insertUser(instaCloneUser)
        if (!wasAcknowledged) {
            call.respond(HttpStatusCode.Conflict, "Failed to sign up user")
            return@post
        }

        call.respond(HttpStatusCode.OK, "Sign up successfully")
    }
}

fun Route.signIn(
    hashingService: HashingService,
    userDataSource: UserDataSource,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {
    post("log-in") {
        val request = call.receiveNullable<LoginRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val user = if (request.username != null) {
            userDataSource.findUserByUsername(request.username)
        } else if (request.email != null) {
            userDataSource.findUserByEmail(request.email)
        } else if (request.mobileNumber != null) {
            userDataSource.findUserByMobileNumber(request.mobileNumber)
        } else {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        if (user == null) {
            call.respond(HttpStatusCode.NotFound, "Can't find account")
            return@post
        }

        val saltedHash = SaltedHash(
            hash = user.password,
            salt = user.salt
        )
        val isCorrectPassword = hashingService.verify(request.password, saltedHash)
        if (!isCorrectPassword) {
            call.respond(HttpStatusCode.BadRequest, "Incorrect password")
            return@post
        }

        val tokenClaim = TokenClaim(name = "userId", value = user._id.toString())
        val token = tokenService.generate(tokenConfig, tokenClaim)

        call.respond(
            status = HttpStatusCode.OK,
            message = AuthResponse(
                token = token,
                id = user._id,
                username = user.username,
                mobileNumber = user.mobileNumber,
                email = user.email,
                fullName = user.fullName,
                birthday = user.birthday,
                agreedToPolicy = user.agreedToPolicy,
                profilePicPath = user.profilePicPath
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