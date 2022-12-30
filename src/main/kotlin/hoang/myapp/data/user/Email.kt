package hoang.myapp.data.user

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.newId

@Serializable
data class Email(
    @Contextual val _id: Id<Email> = newId(),
    var email: String,
    var isVerified: Boolean = false
)