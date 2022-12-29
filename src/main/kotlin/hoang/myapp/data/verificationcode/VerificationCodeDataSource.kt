package hoang.myapp.data.verificationcode

interface VerificationCodeDataSource {
    suspend fun createIndex()
    suspend fun getUserByUsername(emailAddress: String): VerificationCode?
    suspend fun insertVerificationCode(verificationCode: VerificationCode): Boolean
}