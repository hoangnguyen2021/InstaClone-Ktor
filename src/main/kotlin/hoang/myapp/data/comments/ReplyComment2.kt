package hoang.myapp.data.comments

import hoang.myapp.data.user.InstaCloneUser
import kotlinx.datetime.Instant
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.newId

@Serializable
data class ReplyComment2(
    @Contextual val _id: Id<ReplyComment> = newId(),
    val author: InstaCloneUser,
    val content: String,
    val isEdited: Boolean = false,
    val createdAt: Instant,
    val lastEditedAt: Instant,
    val likes: List<String> = emptyList(),
    val tags: List<String> = emptyList()
)