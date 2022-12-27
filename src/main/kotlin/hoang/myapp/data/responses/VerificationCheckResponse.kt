package hoang.myapp.data.responses

import com.twilio.rest.verify.v2.service.VerificationCheck
import hoang.myapp.data.serializers.ZonedDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime

@Serializable
data class VerificationCheckResponse(
    val sid: String? = null,
    val serviceSid: String? = null,
    val accountSid: String? = null,
    val to: String? = null,
    val channel: VerificationCheck.Channel? = null,
    val status: String? = null,
    val valid: Boolean? = null,
    val amount: String? = null,
    val payee: String? = null,
    @Serializable(with = ZonedDateTimeSerializer::class)
    val dateCreated: ZonedDateTime? = null
)
