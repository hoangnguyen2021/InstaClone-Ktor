package hoang.myapp.data.verificationcode

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.newId
import java.time.Instant
import java.time.LocalDateTime

@Serializable
data class VerificationCode(
    @Contextual val _id: Id<VerificationCode> = newId(),
    val code: String,
    val emailAddress: String,
    @Contextual val createdAt: Instant = Instant.now()
)
