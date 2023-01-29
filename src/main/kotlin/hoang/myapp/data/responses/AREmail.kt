package hoang.myapp.data.responses

import kotlinx.serialization.Serializable

@Serializable
data class AREmail(
    val email: String,
    val isVerified: Boolean = false
)
