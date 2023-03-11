package hoang.myapp.data.comments

import hoang.myapp.data.user.InstaCloneUser
import hoang.myapp.data.user.InstaCloneUser2
import kotlinx.datetime.Instant
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.newId

@Serializable
data class Comment(
    @Contextual val _id: Id<Comment> = newId(),
    @Contextual val authorId: Id<InstaCloneUser>,
    val content: String,
    val isEdited: Boolean = false,
    val createdAt: Instant,
    val lastEditedAt: Instant,
    val likes: List<String> = emptyList(),
    val tags: List<String> = emptyList(),
    val replies: List<String> = emptyList()
)

fun Comment.toComment2(author: InstaCloneUser2, replies: List<ReplyComment2>): Comment2 {
    return Comment2(
        _id = _id,
        author = author,
        content = content,
        isEdited = isEdited,
        createdAt = createdAt,
        lastEditedAt = lastEditedAt,
        likes = likes,
        tags = tags,
        replies = replies
    )
}