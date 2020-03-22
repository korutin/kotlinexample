package ru.skillbranch.kotlinexample
import androidx.annotation.VisibleForTesting

object UserHolder {
    private val map = mutableMapOf<String, User>()

    fun registerUser(
        fullName : String,
        email : String,
        password : String
    ): User {
        val us: User = User.makeUser(fullName, email = email, password = password)
        if (us.login in map)
            throw IllegalArgumentException("A user with this email already exists")
        else
            map[us.login] = us
        return us
    }

    fun registerUserByPhone(fullName: String, rawPhone: String):User{
        when (Regex("""^\+[^a-zA-Z0-9]*\d{1}[^a-zA-Z0-9]*\d{1}[^a-zA-Z0-9]*\d{1}[^a-zA-Z0-9]*\d{1}[^a-zA-Z0-9]*\d{1}[^a-zA-Z0-9]*\d{1}[^a-zA-Z0-9]*\d{1}[^a-zA-Z0-9]*\d{1}[^a-zA-Z0-9]*\d{1}[^a-zA-Z0-9]*\d{1}[^a-zA-Z0-9]*\d{1}[^a-zA-Z0-9]*""").matches(rawPhone)) {
            true -> {return User.makeUser(fullName, phone = rawPhone)
                .also {
                    if (it.login in map)
                        throw IllegalArgumentException("A user with this phone already exists")
                    else
                        map[it.login] = it
                }
            }
            false -> {
                throw IllegalArgumentException("Enter a valid phone number starting with a + and containing 11 digits")
            }
        }
    }

    fun loginUser (login:String, password: String ) : String? {
        return map[login]?.run{
            if (checkPassword(password)) this.userInfo
            else null
        }
    }

    fun requestAccessCode(login: String) : Unit {
        val user = map[login]?.let {
            it.setNewAccessCode()
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun clearHolder(){
        map.clear()
    }
}