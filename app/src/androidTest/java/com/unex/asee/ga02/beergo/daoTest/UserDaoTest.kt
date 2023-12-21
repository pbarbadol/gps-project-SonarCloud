import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.unex.asee.ga02.beergo.database.AchievementDao
import com.unex.asee.ga02.beergo.database.BeerGoDatabase
import com.unex.asee.ga02.beergo.database.UserDao
import com.unex.asee.ga02.beergo.model.Achievement
import com.unex.asee.ga02.beergo.model.Beer
import com.unex.asee.ga02.beergo.model.User
import com.unex.asee.ga02.beergo.model.UserAchievementCrossRef
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class UserDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var achievementDao: AchievementDao
    private lateinit var userDao: UserDao
    private lateinit var db: BeerGoDatabase

    @Before
    fun createDb() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            BeerGoDatabase::class.java
        ).allowMainThreadQueries().build()
        db.clearAllTables()
        achievementDao = db.achievementDao()
        userDao = db.userDao()
    }

    @After
    fun closeDb() {
        db.close()
        db.clearAllTables()
    }

    @Test
    fun insertAndRetrieveUser() = runBlocking {

        // Crear un usuario de prueba
        val user = User(name = "TestUser", password = "TestPassword")

        // Insertar el usuario en la base de datos
        val userId = userDao.insert(user)
        user.userId = userId

        // Recuperar el usuario por ID
        val retrievedUser = userDao.findByID(userId)

        // Verificar que el usuario recuperado es igual al usuario insertado
        assertNotNull(retrievedUser)
        assertEquals(user, retrievedUser)
    }

    @Test
    fun insertAndDeleteUser() = runBlocking {
        // Crear un usuario de prueba
        val user = User(name = "TestUser", password = "TestPassword")

        // Insertar el usuario en la base de datos
        val userId = userDao.insert(user)
        user.userId = userId
        // Eliminar el usuario de la base de datos
        userDao.delete(user)

        // Recuperar el usuario por ID después de eliminarlo
        val retrievedUser = userDao.findByID(userId)

        // Verificar que el usuario no existe después de eliminarlo
        assertNull(retrievedUser)
    }



}
