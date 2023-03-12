package hoang.myapp.data.comments

import hoang.myapp.data.user.InstaCloneUser
import org.litote.kmongo.Id

interface ReplyCommentDataSource {
    suspend fun insertReplyComment(replyComment: ReplyComment): Boolean
    suspend fun findReplyCommentsByIds(ids: List<String>): List<ReplyComment>
    suspend fun likeReplyComment(replyCommentId: String, userId: Id<InstaCloneUser>): Boolean
    suspend fun unlikeReplyComment(replyCommentId: String, userId: Id<InstaCloneUser>): Boolean
}