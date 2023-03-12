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
    override suspend fun insertReplyComment(replyComment: ReplyComment): Boolean {
        return replyComments.insertOne(replyComment).wasAcknowledged()
    }

    override suspend fun findReplyCommentsByIds(ids: List<String>): List<ReplyComment> {
        val result = mutableListOf<ReplyComment>()
        ids.forEach { id ->
            replyComments.findOneById(ObjectId(id))?.let { result.add(it) }
        }
        return result
    }

    override suspend fun likeReplyComment(replyCommentId: String, userId: Id<InstaCloneUser>): Boolean {
        return replyComments
            .updateOneById(
                ObjectId(replyCommentId),
                addToSet(Comment::likes, userId.toString())
            )
            .wasAcknowledged()
    }

    override suspend fun unlikeReplyComment(replyCommentId: String, userId: Id<InstaCloneUser>): Boolean {
        return replyComments
            .updateOneById(
                ObjectId(replyCommentId),
                pull(Comment::likes, userId.toString())
            )
            .wasAcknowledged()
    }
}