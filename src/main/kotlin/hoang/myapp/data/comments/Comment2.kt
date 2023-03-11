package hoang.myapp.data.comments

import hoang.myapp.data.user.InstaCloneUser2
import kotlinx.datetime.Instant
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.newId

@Serializable
data class Comment2(
    @Contextual val _id: Id<Comment> = newId(),
    val author: InstaCloneUser2,
    val content: String,
    val isEdited: Boolean = false,
    val createdAt: Instant,
    val lastEditedAt: Instant,
    val likes: List<String> = emptyList(),
    val tags: List<String> = emptyList(),
    val replies: List<ReplyComment2> = emptyList()
)