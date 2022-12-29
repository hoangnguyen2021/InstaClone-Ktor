package hoang.myapp.security.verification

import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator
import java.time.Instant
import javax.crypto.KeyGenerator
import javax.crypto.Mac
import javax.crypto.SecretKey

class OtpGenerator {
    private val totp = TimeBasedOneTimePasswordGenerator()

    fun generateOneTimePassword(): String {
        return totp.generateOneTimePasswordString(generateKey(), Instant.now())
    }

    private fun generateKey(): SecretKey {
        val keyGenerator: KeyGenerator = KeyGenerator.getInstance(totp.algorithm)
        val macLengthInBytes = Mac.getInstance(totp.algorithm).macLength
        keyGenerator.init(macLengthInBytes * 8)
        return keyGenerator.generateKey()
    }
}