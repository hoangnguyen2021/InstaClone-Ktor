package hoang.myapp.plugins

import hoang.myapp.data.user.UserDataSource
import hoang.myapp.routes.*
import hoang.myapp.security.hashing.HashingService
import hoang.myapp.security.token.TokenConfig
import hoang.myapp.security.token.TokenService
import hoang.myapp.twilio.VerificationService
import io.ktor.server.routing.*
import io.ktor.server.application.*

fun Application.configureRouting(
    verificationService: VerificationService,
    userDataSource: UserDataSource,
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {
    routing {
        sendVerificationCode(verificationService)
        checkVerificationCode(verificationService)
        signUp(hashingService, userDataSource)
        signIn(hashingService, userDataSource, tokenService, tokenConfig)
        authenticate()
        getSecretInfo()
    }
}
