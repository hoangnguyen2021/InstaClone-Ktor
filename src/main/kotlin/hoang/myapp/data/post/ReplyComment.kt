package hoang.myapp.data.post

import hoang.myapp.data.user.InstaCloneUser
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
    val createdAt: Instant,
    val lastEditedAt: Instant,
    val likes: Int = 0,
    @Contextual val tags: List<Id<InstaCloneUser>>
)