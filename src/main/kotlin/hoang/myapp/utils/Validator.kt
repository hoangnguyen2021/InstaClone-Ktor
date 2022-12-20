package hoang.myapp.utils

import hoang.myapp.data.requests.AuthRequest
import java.util.regex.Pattern

object Validator {
    fun validateUsername(username: String?): Boolean {
        if (username.isNullOrEmpty()) return false

        val pattern: Pattern = Pattern.compile(
            "^" +
                    "(?=\\S+$)" +        // no white spaces
                    ".{4,}" +            // at least 4 characters
                    "$"
        )
        return pattern.matcher(username).matches()
    }

    fun validatePassword(password: String?): Boolean {
        if (password.isNullOrEmpty()) return false

        val pattern: Pattern = Pattern.compile(
            "^" +
                    "(?=.*[A-Z])" +         // at least 1 uppercase character
                    "(?=.*[0-9])" +         // at least 1 number character
                    "(?=.*[@#$%^&+=])" +    // at least 1 special character
                    "(?=\\S+$)" +           // no white spaces
                    ".{6,}" +               // at least 6 characters
                    "$"
        )
        return pattern.matcher(password).matches()
    }
}