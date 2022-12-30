package hoang.myapp.plugins

import hoang.myapp.data.user.UserDataSource
import hoang.myapp.routes.*
import hoang.myapp.security.hashing.HashingService
import hoang.myapp.security.token.TokenConfig
import hoang.myapp.security.token.TokenService
import hoang.myapp.security.verification.VerificationService
import hoang.myapp.storage.StorageService
import io.ktor.server.routing.*
import io.ktor.server.application.*

fun Application.configureRouting(
    twilioVerificationService: VerificationService,
    sendinBlueTransactionalEmailService: VerificationService,
    userDataSource: UserDataSource,
    storageService: StorageService,
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {
    routing {
        route("/auth") {
            sendVerificationCode(twilioVerificationService, sendinBlueTransactionalEmailService)
            checkVerificationCode(twilioVerificationService, sendinBlueTransactionalEmailService)
            signUp(hashingService, userDataSource)
            signIn(hashingService, userDataSource, tokenService, tokenConfig)
            authenticate()
            getSecretInfo()
        }
        route("/images") {
            uploadProfilePic(storageService)
            getProfilePic(storageService)
        }
    }
}
