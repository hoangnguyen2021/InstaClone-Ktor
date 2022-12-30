package hoang.myapp.data.user

import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class MongoUserDataSource(
    db: CoroutineDatabase
): UserDataSource {
    private val users = db.getCollection<InstaCloneUser>()
    override suspend fun getUserByUsername(username: String): InstaCloneUser? {
        return users.findOne(InstaCloneUser::username eq username)
    }

    override suspend fun insertUser(instaCloneUser: InstaCloneUser): Boolean {
        return users.insertOne(instaCloneUser).wasAcknowledged()
    }
}