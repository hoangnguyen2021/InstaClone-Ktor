package hoang.myapp.data.comment

import hoang.myapp.data.post.Comment
import org.bson.types.ObjectId
import org.litote.kmongo.coroutine.CoroutineDatabase

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
}