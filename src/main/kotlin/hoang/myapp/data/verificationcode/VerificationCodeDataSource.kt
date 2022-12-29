package hoang.myapp.data.verificationcode

interface VerificationCodeDataSource {
    suspend fun getVerificationCodeByEmailAddress(emailAddress: String): VerificationCode?
    suspend fun insertVerificationCode(verificationCode: VerificationCode): Boolean
    suspend fun deleteVerificationCodeByEmailAddress(emailAddress: String): Boolean
}