package hoang.myapp.twilio

import com.twilio.rest.verify.v2.service.Verification
import com.twilio.rest.verify.v2.service.Verification.Channel
import com.twilio.rest.verify.v2.service.VerificationCheck
import hoang.myapp.config.Config.twilioVerifyServiceSid
import kotlinx.coroutines.future.await

class TwilioVerificationService : VerificationService {
    override suspend fun sendVerificationToken(recipient: String, channel: Channel): Verification {
        return Verification.creator(
            twilioVerifyServiceSid,
            recipient,
            channel.toString()
        )
            .createAsync()
            .await()
    }

    override suspend fun checkVerificationToken(recipient: String, tokenToCheck: String): VerificationCheck {
        return VerificationCheck.creator(
            twilioVerifyServiceSid
        )
            .setTo(recipient)
            .setCode(tokenToCheck)
            .createAsync()
            .await()
    }
}