package hoang.myapp.data.posts

import hoang.myapp.data.comments.Comment2
import hoang.myapp.data.user.InstaCloneUser2
import kotlinx.datetime.Instant
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.newId

@Serializable
data class InstaClonePost2(
    @Contextual val _id: Id<InstaClonePost> = newId(),
    val author: InstaCloneUser2,
    val caption: String,
    val isEdited: Boolean = false,
    val createdAt: Instant,
    val lastEditedAt: Instant,
    val mediaPaths: List<String> = emptyList(),
    val likes: List<String> = emptyList(),
    val tags: List<String> = emptyList(),
    val comments: List<Comment2> = emptyList()
)