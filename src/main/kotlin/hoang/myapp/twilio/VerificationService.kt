package hoang.myapp.twilio

import com.twilio.rest.verify.v2.service.Verification
import com.twilio.rest.verify.v2.service.VerificationCheck

interface VerificationService {
    suspend fun sendVerificationToken(recipient: String): Boolean

    suspend fun checkVerificationToken(recipient: String, tokenToCheck: String): Boolean
}