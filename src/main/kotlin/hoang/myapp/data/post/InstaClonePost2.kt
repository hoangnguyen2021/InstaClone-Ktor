package hoang.myapp.data.post

import hoang.myapp.data.comment.Comment
import hoang.myapp.data.user.InstaCloneUser
import kotlinx.datetime.Instant
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.newId

@Serializable
data class InstaClonePost2(
    @Contextual val _id: Id<InstaClonePost> = newId(),
    @Contextual val authorId: Id<InstaCloneUser>,
    val caption: String,
    val isEdited: Boolean = false,
    val createdAt: Instant,
    val lastEditedAt: Instant,
    val mediaPaths: List<String> = emptyList(),
    val likes: List<String> = emptyList(),
    val tags: List<String> = emptyList(),
    val comments: List<Comment> = emptyList()
)