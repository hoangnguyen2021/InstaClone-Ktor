package hoang.myapp.di

import hoang.myapp.config.Config
import hoang.myapp.data.user.MongoUserDataSource
import hoang.myapp.data.user.UserDataSource
import hoang.myapp.security.hashing.HashingService
import hoang.myapp.security.hashing.SHA256HashingService
import hoang.myapp.security.token.JwtTokenService
import hoang.myapp.security.token.TokenConfig
import hoang.myapp.security.token.TokenService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

object KoinModule {
    val appModule = module {
        single {
            KMongo.createClient(Config.connectionString)
                .coroutine
                .getDatabase(Config.dbName)
        }
        singleOf<UserDataSource, CoroutineDatabase>(::MongoUserDataSource)
        singleOf<TokenService>(::JwtTokenService)
        singleOf(::TokenConfig)
        singleOf<HashingService>(::SHA256HashingService)
    }
}