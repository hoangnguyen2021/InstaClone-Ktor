package hoang.myapp.data.user

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.newId

@Serializable
data class InstaCloneUser(
    @Contextual val _id: Id<InstaCloneUser> = newId(),
    val password: String,
    val salt: String,
    val mobileNumber: MobileNumber? = null,
    val email: Email? = null,
    val fullName: String,
    val username: String,
    var birthday: LocalDate,
    var agreedToPolicy: Boolean = false,
    var profilePicPath: String? = null
)