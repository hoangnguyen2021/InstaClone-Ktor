package hoang.myapp.data.comments

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class CommentRequest(
    val authorId: String,
    val postId: String,
    val content: String,
    val isEdited: Boolean = false,
    val createdAt: Instant = Clock.System.now(),
    val lastEditedAt: Instant = Clock.System.now(),
    val tags: List<String> = emptyList()
)