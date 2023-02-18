package hoang.myapp.data.user

import org.bson.types.ObjectId
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.div
import org.litote.kmongo.eq

class MongoUserDataSource(
    db: CoroutineDatabase
): UserDataSource {
    private val users = db.getCollection<InstaCloneUser>()
    override suspend fun getUserById(id: String): InstaCloneUser? {
        return users.findOneById(ObjectId(id))
    }

    override suspend fun getUserByUsername(username: String): InstaCloneUser? {
        return users.findOne(InstaCloneUser::username eq username)
    }

    override suspend fun getUserByMobileNumber(mobileNumber: Long): InstaCloneUser? {
        return users.findOne(InstaCloneUser::mobileNumber / MobileNumber::number eq mobileNumber)
    }

    override suspend fun getUserByEmail(email: String): InstaCloneUser? {
        return users.findOne(InstaCloneUser::email / Email::email eq email)
    }

    override suspend fun insertUser(instaCloneUser: InstaCloneUser): Boolean {
        return users.insertOne(instaCloneUser).wasAcknowledged()
    }
}