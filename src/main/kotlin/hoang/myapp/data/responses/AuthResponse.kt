package hoang.myapp.data.responses

import hoang.myapp.data.user.Email
import hoang.myapp.data.user.MobileNumber
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token: String,
    val username: String,
    val mobileNumber: MobileNumber? = null,
    val email: Email? = null,
    val fullName: String,
    val birthday: LocalDate,
    val agreedToPolicy: Boolean,
    val profilePicPath: String? = null
)