package hoang.myapp.data.user

import org.litote.kmongo.Id

interface UserDataSource {
    suspend fun findUserById(id: String): InstaCloneUser?
    suspend fun findUserByUsername(username: String): InstaCloneUser?
    suspend fun findUserByMobileNumber(mobileNumber: Long): InstaCloneUser?
    suspend fun findUserByEmail(email: String): InstaCloneUser?
    suspend fun findUsersByIds(ids: List<Id<InstaCloneUser>>): List<InstaCloneUser>
    suspend fun insertUser(instaCloneUser: InstaCloneUser): Boolean
}