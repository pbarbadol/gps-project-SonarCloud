import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.unex.asee.ga02.beergo.database.AchievementDao
import com.unex.asee.ga02.beergo.database.BeerGoDatabase
import com.unex.asee.ga02.beergo.model.Achievement
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AchievementDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()


    private lateinit var achievementDao: AchievementDao
    private lateinit var db: BeerGoDatabase

    @Before
    fun createDb() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            BeerGoDatabase::class.java
        ).allowMainThreadQueries().build()

        achievementDao = db.achievementDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertAndGetAllAchievements() = runBlocking {
        // Crear logros de prueba
        val achievement1 = Achievement(1, "Achievement 1", "Description 1", 10, "Category 1")
        val achievement2 = Achievement(2, "Achievement 2", "Description 2", 20, "Category 2")

        // Insertar logros en la base de datos
        achievementDao.insert(achievement1)
        achievementDao.insert(achievement2)

        // Obtener todos los logros y observar los cambios con LiveData
        val allAchievements = MutableLiveData<List<Achievement>>()
        achievementDao.getAll().observeForever { achievements ->
            allAchievements.value = achievements
        }

        // Esperar a que se complete la operación asíncrona
        delay(1000)

        // Verificar que los logros insertados están presentes en la lista
        assertNotNull(allAchievements.value)
        assertEquals(2, allAchievements.value?.size)
        assertTrue(allAchievements.value?.contains(achievement1) == true)
        assertTrue(allAchievements.value?.contains(achievement2) == true)
    }


    @Test
    fun insertAndDeleteAchievement() = runBlocking {
        // Crear logro de prueba
        val achievement = Achievement(1, "Test Achievement", "Test Description", 15, "Test Category")

        // Insertar logro en la base de datos
        achievementDao.insert(achievement)

        // Eliminar el logro de la base de datos
        achievementDao.delete(achievement)

        // Obtener todos los logros y observar los cambios con LiveData
        val allAchievements = achievementDao.getAll()

        // Verificar que la lista está vacía después de eliminar el logro
        assertNull(allAchievements.value)
    }

}
