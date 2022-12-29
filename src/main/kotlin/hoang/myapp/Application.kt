package hoang.myapp

import com.twilio.Twilio
import hoang.myapp.config.Config
import hoang.myapp.data.user.UserDataSource
import hoang.myapp.data.verificationcode.VerificationCodeDataSource
import hoang.myapp.di.KoinModule
import io.ktor.server.application.*
import hoang.myapp.plugins.*
import hoang.myapp.security.hashing.HashingService
import hoang.myapp.security.token.TokenConfig
import hoang.myapp.security.token.TokenService
import hoang.myapp.security.verification.VerificationService
import io.ktor.http.*
import io.ktor.server.resources.*
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.koin
import sendinblue.ApiClient
import sendinblue.Configuration
import sendinblue.auth.ApiKeyAuth

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    // load KoinModule.appModule
    koin {
        modules(KoinModule.appModule)
    }
    val userDataSource: UserDataSource by inject()
    val verificationCodeDataSource: VerificationCodeDataSource by inject()
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
    val twilioVerificationService: VerificationService by inject(qualifier = named("twilio"))
    val sendinBlueTransactionalEmailService: VerificationService by inject(qualifier = named("sendinblue"))

    Twilio.init(Config.TWILIO_USERNAME, Config.TWILIO_PASSWORD)
    val defaultClient: ApiClient = Configuration.getDefaultApiClient()
    val apiKey = defaultClient.getAuthentication("api-key") as ApiKeyAuth
    apiKey.apiKey = Config.SENDIN_BLUE_API_KEY

    configureSecurity(tokenConfig)
    configureMonitoring()
    configureCORS()
    configureSerialization()
    configureResources()
    //configureSockets()
    configureRouting(
        twilioVerificationService,
        sendinBlueTransactionalEmailService,
        userDataSource,
        hashingService,
        tokenService,
        tokenConfig
    )
}
