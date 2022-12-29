package hoang.myapp.security.verification

interface VerificationService {
    suspend fun sendVerificationToken(recipient: String): Boolean

    suspend fun checkVerificationToken(recipient: String, tokenToCheck: String): Boolean
}