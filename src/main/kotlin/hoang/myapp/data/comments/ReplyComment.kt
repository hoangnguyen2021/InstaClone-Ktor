package hoang.myapp.data.comments

import hoang.myapp.data.user.InstaCloneUser
import hoang.myapp.data.user.InstaCloneUser2
import kotlinx.datetime.Instant
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.newId

@Serializable
data class ReplyComment(
    @Contextual val _id: Id<ReplyComment> = newId(),
    @Contextual val authorId: Id<InstaCloneUser>,
    val content: String,
    val isEdited: Boolean = false,
    val createdAt: Instant,
    val lastEditedAt: Instant,
    val likes: List<String> = emptyList(),
    val tags: List<String> = emptyList()
)

fun ReplyComment.toReplyComment2(author: InstaCloneUser2): ReplyComment2 {
    return ReplyComment2(
        _id = _id,
        author = author,
        content = content,
        isEdited = isEdited,
        createdAt = createdAt,
        lastEditedAt = lastEditedAt,
        likes = likes,
        tags = tags
    )
}