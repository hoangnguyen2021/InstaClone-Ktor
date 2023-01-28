package hoang.myapp.data.post

import hoang.myapp.data.user.InstaCloneUser
import kotlinx.datetime.Instant
import kotlinx.serialization.Contextual
import org.litote.kmongo.Id
import org.litote.kmongo.newId

data class InstaClonePost(
    @Contextual val _id: Id<InstaClonePost> = newId(),
    val authorId: Id<InstaCloneUser>,
    val caption: String,
    val createdAt: Instant,
    val lastEditedAt: Instant,
    val mediaPaths: List<String>
)