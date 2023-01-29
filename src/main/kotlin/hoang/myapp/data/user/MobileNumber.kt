package hoang.myapp.data.user

import hoang.myapp.data.responses.ARMobileNumber
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.newId

@Serializable
data class MobileNumber(
    @Contextual val _id: Id<MobileNumber> = newId(),
    val number: Long,
    val isVerified: Boolean = false
) {
    fun toARMobileNumber(): ARMobileNumber {
        return ARMobileNumber(number, isVerified)
    }
}