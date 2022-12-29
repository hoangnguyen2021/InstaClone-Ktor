package hoang.myapp.twilio

import com.twilio.rest.verify.v2.service.Verification
import com.twilio.rest.verify.v2.service.VerificationCheck
import hoang.myapp.config.Config.TWILIO_VERIFY_SERVICE_SID
import hoang.myapp.security.verification.VerificationService
import kotlinx.coroutines.future.await

class TwilioVerificationService : VerificationService {
    override suspend fun sendVerificationToken(recipient: String): Boolean {
        return try {
            Verification.creator(
                TWILIO_VERIFY_SERVICE_SID,
                recipient,
                Verification.Channel.SMS.toString()
            )
                .createAsync()
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun checkVerificationToken(recipient: String, tokenToCheck: String): Boolean {
        return try {
            VerificationCheck.creator(
                TWILIO_VERIFY_SERVICE_SID
            )
                .setTo(recipient)
                .setCode(tokenToCheck)
                .createAsync()
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }
}