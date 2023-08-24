package hoang.myapp.plugins

import hoang.myapp.data.comments.CommentDataSource
import hoang.myapp.data.comments.ReplyCommentDataSource
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
    commentDataSource: CommentDataSource,
    replyCommentDataSource: ReplyCommentDataSource
) {
    routing {
        route("/auth") {
            sendVerificationCode(twilioVerificationService, sendinBlueTransactionalEmailService)
            checkVerificationCode(twilioVerificationService, sendinBlueTransactionalEmailService)
            signUp(hashingService, userDataSource)
            logIn(hashingService, userDataSource, tokenService, tokenConfig)
            authenticate()
            getSecretInfo()
        }
        route("/media") {
            route("/profile-pics") {
                uploadProfilePic(storageService)
                getProfilePic(storageService)
            }
            route("/posts") {
                uploadPostImage(storageService)
                createPost(postDataSource, userDataSource)
                getPostsByUserId(postDataSource, userDataSource, commentDataSource, replyCommentDataSource)
                getPostById(postDataSource, userDataSource, commentDataSource, replyCommentDataSource)
                likePost(postDataSource, userDataSource)
                unlikePost(postDataSource, userDataSource)
                commentOnPost(postDataSource, userDataSource, commentDataSource)
            }
            route("/comments") {
                likeComment(commentDataSource, userDataSource)
                unlikeComment(commentDataSource, userDataSource)
                replyToComment(commentDataSource, userDataSource, replyCommentDataSource)
            }
            route("/reply-comments") {
                likeReplyComment(replyCommentDataSource, userDataSource)
                unlikeReplyComment(replyCommentDataSource, userDataSource)
            }
        }
        route("/users") {
            getUserById(userDataSource)
        }
    }
}
