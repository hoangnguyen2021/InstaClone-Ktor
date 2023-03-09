package hoang.myapp.plugins

import hoang.myapp.data.comments.CommentDataSource
import hoang.myapp.data.posts.PostDataSource
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
    postDataSource: PostDataSource,
    commentDataSource: CommentDataSource
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
                getPostsByUserId(postDataSource, userDataSource, commentDataSource)
                getPostById(postDataSource, commentDataSource)
                getCommentorsByPostId(postDataSource, userDataSource, commentDataSource)
                likePost(postDataSource, userDataSource)
                unlikePost(postDataSource, userDataSource)
                commentOnPost(postDataSource, userDataSource, commentDataSource)
            }
            route("comment") {
                likeComment(commentDataSource, userDataSource)
                unlikeComment(commentDataSource, userDataSource)
            }
        }
        route("/users") {
            getUserById(userDataSource)
        }
    }
}
