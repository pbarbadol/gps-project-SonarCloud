package com.unex.asee.ga02.beergo.utils

class CredentialCheck {
    var fail: Boolean = false
    var msg: String = ""
    var error: CredentialError = CredentialError.PasswordError

    companion object{
        private val TAG = CredentialCheck::class.java.canonicalName
        private val MINCHARS = 4

        private val checks = arrayOf(
            CredentialCheck().apply{
                fail = false
                msg = "Your credentials are OK"
                error = CredentialError.Success
            },
            CredentialCheck().apply{
                fail = true
                msg = "Invalid username"
                error = CredentialError.UsernameError
            },
            CredentialCheck().apply{
                fail = true
                msg = "Invalid password"
                error = CredentialError.PasswordError
            },
            CredentialCheck().apply{
                fail = true
                msg = "Passwords do not match"
                error = CredentialError.PasswordError
            }
        )

        fun login(username: String, password: String): CredentialCheck {
            return if (username.isBlank() || username.length < MINCHARS) checks[1]
            else if (password.isBlank() || password.length < MINCHARS) checks[2]
            else checks[0]
        }

        fun join(username: String, password: String, repassword: String): CredentialCheck {
            return when{
                username.isBlank() || username.length < MINCHARS -> checks[1]
                password.isBlank() || password.length < MINCHARS -> checks[2]
                password!=repassword -> checks[3]
                else -> checks[0]
            }
        }

        fun passwordOk(password1: String, password2: String): CredentialCheck {
            return if (password1!=password2) checks[3]
            else checks[0]
        }
    }

    enum class CredentialError {
        PasswordError, UsernameError, Success
    }
}