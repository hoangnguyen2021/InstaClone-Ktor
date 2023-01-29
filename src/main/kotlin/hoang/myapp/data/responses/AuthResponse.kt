package hoang.myapp.data.responses

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token: String,
    val username: String,
    val mobileNumber: ARMobileNumber? = null,
    val email: AREmail? = null,
    val fullName: String,
    val birthday: LocalDate,
    val agreedToPolicy: Boolean,
    val profilePicPath: String? = null
)