package hoang.myapp.security.verification

import hoang.myapp.data.verificationcode.VerificationCode
import hoang.myapp.data.verificationcode.VerificationCodeDataSource
import sibApi.TransactionalEmailsApi
import sibModel.SendSmtpEmail
import sibModel.SendSmtpEmailTo
import java.util.*

class SendinBlueTransactionalEmailService(
    private val verificationCodeDataSource: VerificationCodeDataSource
) : VerificationService {
    override suspend fun sendVerificationToken(recipient: String): Boolean {
        return try {
            val apiInstance = TransactionalEmailsApi()
            val verificationCode = OtpGenerator().generateOneTimePassword()
            val sendSmtpEmail = SendSmtpEmail().apply {
                to = listOf(SendSmtpEmailTo().email(recipient))
                templateId = 1
                params = Properties().apply {
                    setProperty(VERIFICATION_CODE, verificationCode)
                }
            }
            // java blocking call
            apiInstance.sendTransacEmail(sendSmtpEmail)
            val insertSuccessfully = verificationCodeDataSource.insertVerificationCode(
                VerificationCode(
                    emailAddress = recipient,
                    code = verificationCode
                )
            )
            if (!insertSuccessfully) false
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun checkVerificationToken(recipient: String, tokenToCheck: String): Boolean {
        val verificationCode = verificationCodeDataSource.getVerificationCodeByEmailAddress(recipient) ?: return false

        return if (tokenToCheck == verificationCode.code) {
            val deleteSuccessfully = verificationCodeDataSource.deleteVerificationCodeByEmailAddress(recipient)
            if (!deleteSuccessfully) false
            true
        } else {
            false
        }
    }

    companion object {
        private const val VERIFICATION_CODE = "VERIFICATIONCODE"
    }
}