package hoang.myapp.data.comments

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class ReplyCommentRequest(
    val authorId: String,
    val commentId: String,
    val content: String,
    val isEdited: Boolean = false,
    val createdAt: Instant,
    val lastEditedAt: Instant,
    val tags: List<String> = emptyList()
)