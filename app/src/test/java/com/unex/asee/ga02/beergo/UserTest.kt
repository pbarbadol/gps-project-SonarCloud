import com.unex.asee.ga02.beergo.model.User
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class UserTest {

    private lateinit var user: User

    @Before
    fun init() {
        user = User(userId = 1, name = "user", password = "password")
    }

    @Test
    fun getUserId() {
        assertEquals(1, user.userId)
    }

    @Test
    fun setUserId() {
        assertEquals(1, user.userId)
        user.userId = 2
        assertEquals(2, user.userId)
    }

    @Test
    fun getName() {
        assertEquals("user", user.name)
    }

    @Test
    fun setName() {
        assertEquals("user", user.name)
        user.name = "user2"
        assertEquals("user2", user.name)
    }

    @Test
    fun getPassword() {
        assertEquals("password", user.password)
    }

    @Test
    fun setPassword() {
        assertEquals("password", user.password)
        user.password = "newPassword"
        assertEquals("newPassword", user.password)
    }
}
