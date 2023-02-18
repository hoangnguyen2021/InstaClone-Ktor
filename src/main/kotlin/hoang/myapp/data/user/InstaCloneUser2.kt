package hoang.myapp.data.user

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id

@Serializable
data class InstaCloneUser2(
    @Contextual val _id: Id<InstaCloneUser>,
    val mobileNumber: MobileNumber? = null,
    val email: Email? = null,
    val fullName: String,
    val username: String,
    val birthday: LocalDate,
    val agreedToPolicy: Boolean = false,
    val profilePicPath: String? = null
)