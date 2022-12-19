package hoang.myapp.plugins

import hoang.myapp.authenticate
import hoang.myapp.data.user.UserDataSource
import hoang.myapp.getSecretInfo
import hoang.myapp.security.hashing.HashingService
import hoang.myapp.security.token.TokenConfig
import hoang.myapp.security.token.TokenService
import hoang.myapp.signIn
import hoang.myapp.signUp
import io.ktor.server.routing.*
import io.ktor.server.application.*

fun Application.configureRouting(
    userDataSource: UserDataSource,
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {

    routing {
        signUp(hashingService, userDataSource)
        signIn(hashingService, userDataSource, tokenService, tokenConfig)
        authenticate()
        getSecretInfo()
    }
}
