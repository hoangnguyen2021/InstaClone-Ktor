package hoang.myapp.data.requests

import hoang.myapp.data.user.Email
import hoang.myapp.data.user.InstaCloneUser
import hoang.myapp.data.user.MobileNumber
import hoang.myapp.security.hashing.SaltedHash
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class SignupRequest(
    var mobileNumber: Long? = null,
    var isMobileNumberVerified: Boolean = false,
    var email: String? = null,
    var isEmailVerified: Boolean = false,
    var fullName: String,
    var birthday: LocalDate,
    var username: String,
    var password: String,
    var agreedToPolicy: Boolean = false,
    var profilePicPath: String? = null
) {
    fun mapToInstaCloneUser(saltedHash: SaltedHash): InstaCloneUser {
        return InstaCloneUser(
            mobileNumber =
            if (mobileNumber != null) {
                MobileNumber(
                    number = mobileNumber!!,
                    isVerified = isMobileNumberVerified
                )
            } else { null },
            email =
            if (email != null) {
                Email(
                    email = email!!,
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
