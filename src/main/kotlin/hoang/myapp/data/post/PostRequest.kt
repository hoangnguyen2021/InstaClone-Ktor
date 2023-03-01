package hoang.myapp.data.post

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class PostRequest(
    val caption: String,
    val authorUsername: String,
    val createdAt: Instant,
    val lastEditedAt: Instant,
    val mediaPaths: List<String> = emptyList()
)