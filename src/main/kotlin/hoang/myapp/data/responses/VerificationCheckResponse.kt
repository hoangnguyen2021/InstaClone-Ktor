package hoang.myapp.data.responses

import com.twilio.rest.verify.v2.service.VerificationCheck
import kotlinx.serialization.Serializable

@Serializable
data class VerificationCheckResponse(
    val sid: String? = null,
    val serviceSid: String? = null,
    val accountSid: String? = null,
    val to: String? = null,
    val channel: VerificationCheck.Channel? = null,
    val status: String? = null,
    private val valid: Boolean? = null,
    private val amount: String? = null,
    private val payee: String? = null,
)
