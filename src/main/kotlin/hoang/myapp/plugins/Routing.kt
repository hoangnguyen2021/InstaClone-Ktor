package hoang.myapp.plugins

import hoang.myapp.data.post.PostDataSource
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
    tokenConfig: TokenConfig,
    postDataSource: PostDataSource
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
        route("/media") {
            route("/profile-pic") {
                uploadProfilePic(storageService)
                getProfilePic(storageService)
            }
            route("/post") {
                uploadPostImage(storageService)
                createPost(postDataSource, userDataSource)
                getPostsByUser(postDataSource, userDataSource)
                getPostById(postDataSource)
            }
        }
        route("/users") {
            getUserById(userDataSource)
        }
    }
}
