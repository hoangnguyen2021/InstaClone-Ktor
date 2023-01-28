package hoang.myapp.data.post

interface PostDataSource {
    suspend fun insertPost(instaClonePost: InstaClonePost): Boolean
}