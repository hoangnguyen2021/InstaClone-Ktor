package hoang.myapp.twilio

import com.twilio.rest.verify.v2.service.Verification
import com.twilio.rest.verify.v2.service.VerificationCheck
import com.twilio.type.PhoneNumber

interface VerificationService {
    suspend fun sendVerificationToken(recipient: String, channel: Verification.Channel): Verification

    suspend fun checkVerificationToken(recipient: String, tokenToCheck: String): VerificationCheck
}