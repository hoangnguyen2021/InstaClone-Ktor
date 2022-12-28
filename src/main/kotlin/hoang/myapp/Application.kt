package hoang.myapp

import com.twilio.Twilio
import hoang.myapp.config.Config
import hoang.myapp.data.user.UserDataSource
import hoang.myapp.di.KoinModule
import io.ktor.server.application.*
import hoang.myapp.plugins.*
import hoang.myapp.security.hashing.HashingService
import hoang.myapp.security.token.TokenConfig
import hoang.myapp.security.token.TokenService
import hoang.myapp.twilio.VerificationService
import io.ktor.http.*
import io.ktor.server.resources.*
import org.koin.core.parameter.parametersOf
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.koin

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    // load KoinModule.appModule
    koin {
        modules(KoinModule.appModule)
    }
    val userDataSource: UserDataSource by inject()
    val tokenService: TokenService by inject()
    val tokenConfig: TokenConfig by inject {
        parametersOf(
            Config.jwtSecret,
            environment.config.property("jwt.issuer").getString(),
            environment.config.property("jwt.audience").getString(),
            environment.config.property("jwt.realm").getString(),
            365L * 60L * 60L * 24L * 1000L,
        )
    }
    val hashingService: HashingService by inject()
    val verificationService: VerificationService by inject()
    Twilio.init(Config.twilioUsername, Config.twilioPassword)

    configureSecurity(tokenConfig)
    configureMonitoring()
    configureCORS()
    configureSerialization()
    configureResources()
    //configureSockets()
    configureRouting(verificationService, userDataSource, hashingService, tokenService, tokenConfig)
}
