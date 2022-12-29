package hoang.myapp.data.verificationcode

import com.mongodb.client.model.IndexOptions

import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import java.util.concurrent.TimeUnit

class MongoVerificationCodeDataSource(
    db: CoroutineDatabase
): VerificationCodeDataSource {
    private val verificationCodes = db.getCollection<VerificationCode>()

    override suspend fun createIndex() {
        verificationCodes.createIndex("{'createdAt': 1}", IndexOptions().expireAfter(2, TimeUnit.MINUTES))
    }

    override suspend fun getUserByUsername(emailAddress: String): VerificationCode? {
        return verificationCodes.findOne(VerificationCode::code eq emailAddress)
    }

    override suspend fun insertVerificationCode(verificationCode: VerificationCode): Boolean {
        return verificationCodes.insertOne(verificationCode).wasAcknowledged()
    }
}