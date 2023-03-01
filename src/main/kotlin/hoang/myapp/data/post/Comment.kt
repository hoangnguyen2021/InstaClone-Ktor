package hoang.myapp.data.post

import hoang.myapp.data.user.InstaCloneUser
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
    val likes: Int = 0,
    val tags: List<String> = emptyList(),
    val replies: List<ReplyComment> = emptyList()
)