package hoang.myapp.data.post

import hoang.myapp.data.user.InstaCloneUser
import org.litote.kmongo.Id

interface PostDataSource {
    suspend fun insertPost(instaClonePost: InstaClonePost): Boolean
    suspend fun getPostsByUser(authorId: Id<InstaCloneUser>): List<InstaClonePost>
    suspend fun getPostById(id: String): InstaClonePost?
    suspend fun likePost(id: String, userId: Id<InstaCloneUser>): Boolean
    suspend fun unlikePost(id: String, userId: Id<InstaCloneUser>): Boolean
}