package hoang.myapp.di

import hoang.myapp.config.Config
import hoang.myapp.data.user.MongoUserDataSource
import hoang.myapp.data.user.UserDataSource
import hoang.myapp.data.verificationcode.MongoVerificationCodeDataSource
import hoang.myapp.data.verificationcode.VerificationCodeDataSource
import hoang.myapp.security.hashing.HashingService
import hoang.myapp.security.hashing.SHA256HashingService
import hoang.myapp.security.token.JwtTokenService
import hoang.myapp.security.token.TokenConfig
import hoang.myapp.security.token.TokenService
import hoang.myapp.twilio.TwilioVerificationService
import hoang.myapp.security.verification.VerificationService
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import hoang.myapp.sendinblue.SendinBlueTransactionalEmailService


object KoinModule {
    val appModule = module {
        single {
            KMongo.createClient(Config.connectionString)
                .coroutine
                .getDatabase(Config.DB_NAME)
        }
        singleOf<UserDataSource, CoroutineDatabase>(::MongoUserDataSource)
        singleOf<VerificationCodeDataSource, CoroutineDatabase>(::MongoVerificationCodeDataSource)
        singleOf<TokenService>(::JwtTokenService)
        singleOf(::TokenConfig)
        singleOf<HashingService>(::SHA256HashingService)
        single<VerificationService>(named("twilio")){ TwilioVerificationService() }
        single<VerificationService>(named("sendinblue")) {
            SendinBlueTransactionalEmailService(get())
        }
    }
}