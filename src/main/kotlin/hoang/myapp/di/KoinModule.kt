package hoang.myapp.di

import aws.sdk.kotlin.runtime.auth.credentials.EnvironmentCredentialsProvider
import aws.sdk.kotlin.services.s3.S3Client
import hoang.myapp.config.Config.AWS_REGION
import hoang.myapp.config.Config.MONGODB_DB_NAME
import hoang.myapp.config.Config.MONGODB_CONNECTION_STRING
import hoang.myapp.data.comment.CommentDataSource
import hoang.myapp.data.comment.MongoCommentDataSource
import hoang.myapp.data.post.MongoPostDataSource
import hoang.myapp.data.post.PostDataSource
import hoang.myapp.data.user.MongoUserDataSource
import hoang.myapp.data.user.UserDataSource
import hoang.myapp.data.verificationcode.MongoVerificationCodeDataSource
import hoang.myapp.data.verificationcode.VerificationCodeDataSource
import hoang.myapp.security.hashing.HashingService
import hoang.myapp.security.hashing.Password4jHashingService
import hoang.myapp.security.token.JwtTokenService
import hoang.myapp.security.token.TokenConfig
import hoang.myapp.security.token.TokenService
import hoang.myapp.security.verification.TwilioVerificationService
import hoang.myapp.security.verification.VerificationService
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import hoang.myapp.security.verification.SendinBlueTransactionalEmailService
import hoang.myapp.storage.AmazonS3Service
import hoang.myapp.storage.StorageService

object KoinModule {
    val appModule = module {
        single {
            KMongo.createClient(MONGODB_CONNECTION_STRING)
                .coroutine
                .getDatabase(MONGODB_DB_NAME)
        }
        single {
            S3Client {
                region = AWS_REGION
                credentialsProvider = EnvironmentCredentialsProvider()
            }
        }
        single<VerificationService>(named("twilio")){ TwilioVerificationService() }
        single<VerificationService>(named("sendinblue")) {
            SendinBlueTransactionalEmailService(get())
        }
        singleOf<StorageService, S3Client>(::AmazonS3Service)
        singleOf<UserDataSource, CoroutineDatabase>(::MongoUserDataSource)
        singleOf<VerificationCodeDataSource, CoroutineDatabase>(::MongoVerificationCodeDataSource)
        singleOf<TokenService>(::JwtTokenService)
        singleOf(::TokenConfig)
        singleOf<HashingService>(::Password4jHashingService)
        singleOf<PostDataSource, CoroutineDatabase>(::MongoPostDataSource)
        singleOf<CommentDataSource, CoroutineDatabase>(::MongoCommentDataSource)
    }
}