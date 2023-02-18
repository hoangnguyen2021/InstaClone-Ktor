package hoang.myapp.data.post

import hoang.myapp.data.user.InstaCloneUser
import org.bson.types.ObjectId
import org.litote.kmongo.Id
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class MongoPostDataSource(
    db: CoroutineDatabase
): PostDataSource {
    private val posts = db.getCollection<InstaClonePost>()
    override suspend fun insertPost(instaClonePost: InstaClonePost): Boolean {
        return posts.insertOne(instaClonePost).wasAcknowledged()
    }

    override suspend fun getPostsByUser(authorId: Id<InstaCloneUser>): List<InstaClonePost> {
        return posts
            .find(InstaClonePost::authorId eq authorId)
            .descendingSort(InstaClonePost::createdAt)
            .toList()
    }

    override suspend fun getPostById(id: String): InstaClonePost? {
        return posts.findOneById(ObjectId(id))
    }
}