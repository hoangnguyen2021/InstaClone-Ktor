package hoang.myapp.data.post

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class CommentRequest(
    val authorId: String,
    val postId: String,
    val content: String,
    val isEdited: Boolean = false,
    val createdAt: Instant,
    val lastEditedAt: Instant,
    val likes: Int = 0,
    val tags: List<String> = emptyList()
)