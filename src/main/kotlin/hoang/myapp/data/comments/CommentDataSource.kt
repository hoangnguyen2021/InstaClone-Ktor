package hoang.myapp.data.comments

import hoang.myapp.data.user.InstaCloneUser
import org.litote.kmongo.Id

interface CommentDataSource {
    suspend fun insertComment(comment: Comment): Boolean
    suspend fun findCommentsByIds(ids: List<String>): List<Comment>
    suspend fun likeComment(commentId: String, userId: Id<InstaCloneUser>): Boolean
    suspend fun unlikeComment(commentId: String, userId: Id<InstaCloneUser>): Boolean
}