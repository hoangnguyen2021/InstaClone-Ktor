package hoang.myapp.data.verificationcode

import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class MongoVerificationCodeDataSource(
    db: CoroutineDatabase
): VerificationCodeDataSource {
    private val verificationCodes = db.getCollection<VerificationCode>()

    override suspend fun getVerificationCodeByEmailAddress(emailAddress: String): VerificationCode? {
        return verificationCodes.findOne(VerificationCode::emailAddress eq emailAddress)
    }

    override suspend fun insertVerificationCode(verificationCode: VerificationCode): Boolean {
        return verificationCodes.insertOne(verificationCode).wasAcknowledged()
    }

    override suspend fun deleteVerificationCodeByEmailAddress(emailAddress: String): Boolean {
        return verificationCodes.deleteOne(VerificationCode::emailAddress eq emailAddress).wasAcknowledged()
    }
}