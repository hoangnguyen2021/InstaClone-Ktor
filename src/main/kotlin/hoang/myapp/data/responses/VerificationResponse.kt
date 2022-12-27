package hoang.myapp.data.responses

import com.twilio.rest.verify.v2.service.Verification
import kotlinx.serialization.Serializable

@Serializable
data class VerificationResponse(
    val sid: String? = null,
    val serviceSid: String? = null,
    val accountSid: String? = null,
    val to: String? = null,
    val channel: Verification.Channel? = null,
    val status: String? = null,
    private val valid: Boolean? = null,
    private val amount: String? = null,
    private val payee: String? = null,
)
