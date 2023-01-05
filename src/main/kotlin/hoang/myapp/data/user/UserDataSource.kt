package hoang.myapp.data.user

interface UserDataSource {
    suspend fun getUserByUsername(username: String): InstaCloneUser?
    suspend fun getUserByMobileNumber(mobileNumber: Long): InstaCloneUser?
    suspend fun getUserByEmail(email: String): InstaCloneUser?
    suspend fun insertUser(instaCloneUser: InstaCloneUser): Boolean
}