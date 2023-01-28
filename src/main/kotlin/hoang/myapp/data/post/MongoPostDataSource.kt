package hoang.myapp.data.post

import org.litote.kmongo.coroutine.CoroutineDatabase

class MongoPostDataSource(
    db: CoroutineDatabase
): PostDataSource {
    private val posts = db.getCollection<InstaClonePost>()
    override suspend fun insertPost(instaClonePost: InstaClonePost): Boolean {
        return posts.insertOne(instaClonePost).wasAcknowledged()
    }
}