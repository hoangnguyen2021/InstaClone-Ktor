package hoang.myapp.data.posts

import hoang.myapp.data.comments.Comment2
import hoang.myapp.data.user.InstaCloneUser
import kotlinx.datetime.Instant
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.newId

@Serializable
data class InstaClonePost(
    @Contextual val _id: Id<InstaClonePost> = newId(),
    @Contextual val authorId: Id<InstaCloneUser>,
    val caption: String,
    val isEdited: Boolean = false,
    val createdAt: Instant,
    val lastEditedAt: Instant,
    val mediaPaths: List<String> = emptyList(),
    val likes: List<String> = emptyList(),
    val tags: List<String> = emptyList(),
    val comments: List<String> = emptyList()
)

fun InstaClonePost.toInstaClonePost2(comments: List<Comment2>): InstaClonePost2 {
    return InstaClonePost2(
        _id = _id,
        authorId = authorId,
        caption = caption,
        isEdited = isEdited,
        createdAt = createdAt,
        lastEditedAt = lastEditedAt,
        mediaPaths = mediaPaths,
        likes = likes,
        tags = tags,
        comments = comments
    )
}