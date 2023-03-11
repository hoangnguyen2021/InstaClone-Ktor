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
    val birthday: LocalDate,
    val agreedToPolicy: Boolean = false,
    val profilePicPath: String? = null,
    val followers: List<String> = emptyList(),
    val following: List<String> = emptyList()
)

fun InstaCloneUser.toInstaCloneUser2(): InstaCloneUser2 {
    return InstaCloneUser2(
        _id = _id,
        mobileNumber = mobileNumber,
        email = email,
        fullName = fullName,
        username = username,
        birthday = birthday,
        agreedToPolicy = agreedToPolicy,
        profilePicPath = profilePicPath
    )
}