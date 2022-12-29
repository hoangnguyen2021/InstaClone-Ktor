package sendinblue

import hoang.myapp.twilio.VerificationService
import sibApi.TransactionalEmailsApi
import sibModel.SendSmtpEmail
import sibModel.SendSmtpEmailTo
import java.util.*


class SendinBlueTransactionalEmailService : VerificationService {
    override suspend fun sendVerificationToken(recipient: String): Boolean {
        return try {
            val apiInstance = TransactionalEmailsApi()
            val sendSmtpEmail = SendSmtpEmail().apply {
                to = listOf(SendSmtpEmailTo().email(recipient))
                templateId = 1
                params = Properties().apply {
                    setProperty("VERIFICATIONCODE", "123456")
                }
            }
            // blocking call
            apiInstance.sendTransacEmail(sendSmtpEmail)
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun checkVerificationToken(recipient: String, tokenToCheck: String): Boolean {
        TODO("Not yet implemented")
    }
}