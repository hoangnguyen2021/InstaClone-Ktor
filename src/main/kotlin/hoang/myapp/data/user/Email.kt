package hoang.myapp.data.user

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.newId

@Serializable
data class Email(
    @Contextual val _id: Id<Email> = newId(),
    val email: String,
    val isVerified: Boolean = false
)