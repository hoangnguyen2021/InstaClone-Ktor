package hoang.myapp.data.user

interface UserDataSource {
    suspend fun getUserByUsername(username: String): InstaCloneUser?
    suspend fun insertUser(instaCloneUser: InstaCloneUser): Boolean
}