package hoang.myapp.data.comments

import hoang.myapp.data.user.InstaCloneUser
import org.bson.types.ObjectId
import org.litote.kmongo.Id
import org.litote.kmongo.addToSet
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.pull

class MongoReplyCommentDataSource(
    db: CoroutineDatabase
): ReplyCommentDataSource {
    private val replyComments = db.getCollection<ReplyComment>()
    override suspend fun insertReplyComment(comment: ReplyComment): Boolean {
        return replyComments.insertOne(comment).wasAcknowledged()
    }

    override suspend fun findReplyCommentsByIds(ids: List<String>): List<ReplyComment> {
        val result = mutableListOf<ReplyComment>()
        ids.forEach { id ->
            replyComments.findOneById(ObjectId(id))?.let { result.add(it) }
        }
        return result
    }

    override suspend fun likeReplyComment(commentId: String, userId: Id<InstaCloneUser>): Boolean {
        return replyComments
            .updateOneById(
                ObjectId(commentId),
                addToSet(Comment::likes, userId.toString())
            )
            .wasAcknowledged()
    }

    override suspend fun unlikeReplyComment(commentId: String, userId: Id<InstaCloneUser>): Boolean {
        return replyComments
            .updateOneById(
                ObjectId(commentId),
                pull(Comment::likes, userId.toString())
            )
            .wasAcknowledged()
    }
}