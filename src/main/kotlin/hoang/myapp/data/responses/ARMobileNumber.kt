package hoang.myapp.data.responses

import kotlinx.serialization.Serializable

@Serializable
data class ARMobileNumber(
    val number: Long,
    val isVerified: Boolean = false
)
