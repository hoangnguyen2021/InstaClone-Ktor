package hoang.myapp.data.responses

import hoang.myapp.data.user.Email
import hoang.myapp.data.user.InstaCloneUser
import hoang.myapp.data.user.MobileNumber
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id

@Serializable
data class AuthResponse(
    val token: String,
    @Contextual val id: Id<InstaCloneUser>,
    val username: String,
    val mobileNumber: MobileNumber? = null,
    val email: Email? = null,
    val fullName: String,
    val birthday: LocalDate,
    val agreedToPolicy: Boolean,
    val profilePicPath: String? = null,
    val followers: List<String> = emptyList(),
    val following: List<String> = emptyList()
)