package hoang.myapp.twilio

import com.twilio.rest.api.v2010.account.Message
import com.twilio.rest.verify.v2.service.Verification
import com.twilio.rest.verify.v2.service.Verification.Channel
import com.twilio.rest.verify.v2.service.VerificationCheck
import com.twilio.type.PhoneNumber
import hoang.myapp.config.Config.twilioVerifyServiceSid
import kotlinx.coroutines.future.await


class TwilioVerificationService : VerificationService {
    override suspend fun sendVerificationToken(phoneNumber: PhoneNumber, channel: Channel): Verification {
        return Verification.creator(
            twilioVerifyServiceSid,
            phoneNumber.toString(),
            channel.toString()
        )
            .createAsync()
            .await()
    }

    override suspend fun checkVerificationToken(phoneNumber: PhoneNumber, tokenToCheck: String): VerificationCheck {
        return VerificationCheck.creator(
            twilioVerifyServiceSid
        )
            .setTo(phoneNumber.toString())
            .setCode(tokenToCheck)
            .createAsync()
            .await()
    }
}