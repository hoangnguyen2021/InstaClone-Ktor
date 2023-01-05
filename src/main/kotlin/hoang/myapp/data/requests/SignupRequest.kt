package hoang.myapp.data.requests

import hoang.myapp.data.user.Email
import hoang.myapp.data.user.InstaCloneUser
import hoang.myapp.data.user.MobileNumber
import hoang.myapp.security.hashing.SaltedHash
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class SignupRequest(
    val mobileNumber: Long? = null,
    val isMobileNumberVerified: Boolean = false,
    val email: String? = null,
    val isEmailVerified: Boolean = false,
    val fullName: String,
    val birthday: LocalDate,
    val username: String,
    val password: String,
    val agreedToPolicy: Boolean = false,
    val profilePicPath: String? = null
) {
    fun mapToInstaCloneUser(saltedHash: SaltedHash): InstaCloneUser {
        return InstaCloneUser(
            mobileNumber =
            if (mobileNumber != null) {
                MobileNumber(
                    number = mobileNumber,
                    isVerified = isMobileNumberVerified
                )
            } else { null },
            email =
            if (email != null) {
                Email(
                    email = email,
                    isVerified = isEmailVerified
                )
            } else { null },
            password = saltedHash.hash,
            salt = saltedHash.salt,
            username = username,
            birthday = birthday,
            fullName = fullName,
            agreedToPolicy = agreedToPolicy,
            profilePicPath = profilePicPath
        )
    }
}
