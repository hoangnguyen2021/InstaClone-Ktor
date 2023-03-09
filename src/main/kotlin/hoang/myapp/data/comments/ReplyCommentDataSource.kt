package hoang.myapp.data.comments

import hoang.myapp.data.user.InstaCloneUser
import org.litote.kmongo.Id

interface ReplyCommentDataSource {
    suspend fun insertReplyComment(comment: ReplyComment): Boolean
    suspend fun findReplyCommentsByIds(ids: List<String>): List<ReplyComment>
    suspend fun likeReplyComment(commentId: String, userId: Id<InstaCloneUser>): Boolean
    suspend fun unlikeReplyComment(commentId: String, userId: Id<InstaCloneUser>): Boolean
}