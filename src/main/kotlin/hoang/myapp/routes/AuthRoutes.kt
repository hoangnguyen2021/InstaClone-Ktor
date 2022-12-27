package hoang.myapp.routes

import com.twilio.rest.verify.v2.service.Verification
import com.twilio.type.PhoneNumber
import hoang.myapp.data.requests.AuthRequest
import hoang.myapp.data.responses.AuthResponse
import hoang.myapp.data.responses.VerificationCheckResponse
import hoang.myapp.data.responses.VerificationResponse
import hoang.myapp.data.user.User
import hoang.myapp.data.user.UserDataSource
import hoang.myapp.security.hashing.HashingService
import hoang.myapp.security.hashing.SaltedHash
import hoang.myapp.security.token.TokenClaim
import hoang.myapp.security.token.TokenConfig
import hoang.myapp.security.token.TokenService
import hoang.myapp.twilio.VerificationService
import hoang.myapp.utils.Validator
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.sendVerificationCode(
    verificationService: VerificationService
) {
    get("send-verification-code") {
        val mobileNumber = call.request.queryParameters["mobile-number"]
        if (mobileNumber == null) {
            call.respond(HttpStatusCode.BadRequest, "Missing parameters")
            return@get
        }

        val phoneNumber = PhoneNumber("+1${mobileNumber}")
        val verification = verificationService.sendVerificationToken(phoneNumber, Verification.Channel.SMS)

        call.respond(
            HttpStatusCode.OK,
            VerificationResponse(
                sid = verification.sid,
                serviceSid = verification.serviceSid,
                accountSid = verification.accountSid,
                to = verification.to,
                channel = verification.channel,
                status = verification.status,
                valid = verification.valid,
                amount = verification.amount,
                payee = verification.payee,
                dateCreated = verification.dateCreated
            )
        )
    }
}

fun Route.checkVerificationCode(
    verificationService: VerificationService
) {
    get("check-verification-code") {
        val mobileNumber = call.request.queryParameters["mobile-number"]
        val code = call.request.queryParameters["code"]
        if (mobileNumber == null || code == null) {
            call.respond(HttpStatusCode.BadRequest, "Missing parameters")
            return@get
        }

        val phoneNumber = PhoneNumber("+1${mobileNumber}")
        val verificationCheck = verificationService.checkVerificationToken(phoneNumber, code)

        call.respond(
            HttpStatusCode.OK,
            VerificationCheckResponse(
                sid = verificationCheck.sid,
                serviceSid = verificationCheck.serviceSid,
                accountSid = verificationCheck.accountSid,
                to = verificationCheck.to,
                channel = verificationCheck.channel,
                status = verificationCheck.status,
                valid = verificationCheck.valid,
                amount = verificationCheck.amount,
                payee = verificationCheck.payee,
                dateCreated = verificationCheck.dateCreated
            )
        )
    }
}

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
            call.respond(
                HttpStatusCode.BadRequest,
                "Username must be at 4 characters or longer and contain no whitespace"
            )
            return@post
        }
        if (!Validator.validatePassword(request.password)) {
            call.respond(
                HttpStatusCode.BadRequest,
                "Password must be at 6 characters or longer and contain at least 1 uppercase character, 1 number, 1 special character, and no whitespace"
            )
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

        call.respond(HttpStatusCode.OK, "Sign up successfully")
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
            call.respond(HttpStatusCode.Conflict, "This username is not registered")
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