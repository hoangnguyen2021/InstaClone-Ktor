package hoang.myapp.data.post

import hoang.myapp.data.user.InstaCloneUser
import org.bson.types.ObjectId
import org.litote.kmongo.*
import org.litote.kmongo.coroutine.CoroutineDatabase

class MongoPostDataSource(
    db: CoroutineDatabase
) : PostDataSource {
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

    override suspend fun likePost(postId: String, userId: Id<InstaCloneUser>): Boolean {
        return posts
            .updateOneById(
                ObjectId(postId),
                addToSet(InstaClonePost::likes, userId.toString())
            )
            .wasAcknowledged()
    }

    override suspend fun unlikePost(postId: String, userId: Id<InstaCloneUser>): Boolean {
        return posts
            .updateOneById(
                ObjectId(postId),
                pull(InstaClonePost::likes, userId.toString())
            )
            .wasAcknowledged()
    }

    override suspend fun commentOnPost(postId: String, commentId: String): Boolean {
        return posts
            .updateOneById(
                ObjectId(postId),
                push(InstaClonePost::comments, commentId)
            )
            .wasAcknowledged()
    }
}