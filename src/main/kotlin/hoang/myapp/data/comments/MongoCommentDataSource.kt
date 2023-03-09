package hoang.myapp.data.comments

import hoang.myapp.data.user.InstaCloneUser
import org.bson.types.ObjectId
import org.litote.kmongo.Id
import org.litote.kmongo.addToSet
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.pull

class MongoCommentDataSource(
    db: CoroutineDatabase
): CommentDataSource {
    private val comments = db.getCollection<Comment>()
    override suspend fun insertComment(comment: Comment): Boolean {
        return comments.insertOne(comment).wasAcknowledged()
    }

    override suspend fun findCommentsByIds(ids: List<String>): List<Comment> {
        val result = mutableListOf<Comment>()
        ids.forEach { id ->
            comments.findOneById(ObjectId(id))?.let { result.add(it) }
        }
        return result
    }

    override suspend fun likeComment(commentId: String, userId: Id<InstaCloneUser>): Boolean {
        return comments
            .updateOneById(
                ObjectId(commentId),
                addToSet(Comment::likes, userId.toString())
            )
            .wasAcknowledged()
    }

    override suspend fun unlikeComment(commentId: String, userId: Id<InstaCloneUser>): Boolean {
        return comments
            .updateOneById(
                ObjectId(commentId),
                pull(Comment::likes, userId.toString())
            )
            .wasAcknowledged()
    }
}