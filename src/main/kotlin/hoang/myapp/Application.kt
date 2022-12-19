package hoang.myapp

import hoang.myapp.configs.Config
import hoang.myapp.data.user.MongoUserDataSource
import io.ktor.server.application.*
import hoang.myapp.plugins.*
import hoang.myapp.security.hashing.SHA256HashingService
import hoang.myapp.security.token.JwtTokenService
import hoang.myapp.security.token.TokenConfig
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

fun main(args: Array<String>): Unit =
        io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    val db = KMongo.createClient(Config.connectionString)
        .coroutine
        .getDatabase(Config.dbName)

    val userDataSource = MongoUserDataSource(db)
    val tokenService = JwtTokenService()
    val tokenConfig = TokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        expiresIn = 365L * 60L * 60L * 24L * 1000L,
        secret = System.getenv("JWT_SECRET")
    )
    val hashingService = SHA256HashingService()

    configureSecurity(tokenConfig)
    configureMonitoring()
    configureSerialization()
    configureSockets()
    configureRouting(userDataSource, hashingService, tokenService, tokenConfig)
}
