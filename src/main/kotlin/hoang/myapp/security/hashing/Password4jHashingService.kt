package hoang.myapp.security.hashing

import com.password4j.Password

class Password4jHashingService: HashingService {
    override fun generateSaltedHash(password: String, saltLength: Int): SaltedHash {
        val hash = Password.hash(password).addRandomSalt(saltLength).withArgon2()
        return SaltedHash(
            hash = hash.result,
            salt = hash.salt
        )
    }

    override fun verify(password: String, saltedHash: SaltedHash): Boolean {
        return Password
            .check(password, saltedHash.hash)
            .addSalt(saltedHash.salt)
            .withArgon2()
    }
}