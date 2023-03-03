package hoang.myapp.data.user

import org.litote.kmongo.Id

interface UserDataSource {
    suspend fun getUserById(id: String): InstaCloneUser?
    suspend fun getUserByUsername(username: String): InstaCloneUser?
    suspend fun getUserByMobileNumber(mobileNumber: Long): InstaCloneUser?
    suspend fun getUserByEmail(email: String): InstaCloneUser?
    suspend fun getUsersByIds(ids: List<Id<InstaCloneUser>>): List<InstaCloneUser>
    suspend fun insertUser(instaCloneUser: InstaCloneUser): Boolean
}