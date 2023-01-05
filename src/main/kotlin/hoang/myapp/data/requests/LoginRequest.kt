package hoang.myapp.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val mobileNumber: Long? = null,
    val email: String? = null,
    val username: String? = null,
    val password: String
)
