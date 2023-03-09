package hoang.myapp.data.comment

import hoang.myapp.data.post.Comment

interface CommentDataSource {
    suspend fun insertComment(comment: Comment): Boolean
    suspend fun findCommentsByIds(ids: List<String>): List<Comment>
}