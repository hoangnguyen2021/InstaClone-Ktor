package hoang.myapp.security.token

data class TokenConfig(
    val secret: String,
    val issuer: String,
    val audience: String,
    val realm: String,
    val expiresIn: Long
)
