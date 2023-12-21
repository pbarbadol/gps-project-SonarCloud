import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.unex.asee.ga02.beergo.database.BeerDao
import com.unex.asee.ga02.beergo.database.BeerGoDatabase
import com.unex.asee.ga02.beergo.model.Beer
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class BeerDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()


    private lateinit var beerDao: BeerDao
    private lateinit var db: BeerGoDatabase

    @Before
    fun createDb() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            BeerGoDatabase::class.java
        ).allowMainThreadQueries().build()

        beerDao = db.beerDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertAndGetAllBeers() = runBlocking {
        // Crear cervezas de prueba
        val beer1 = Beer(
            beerId = 1,
            title = "Test Beer 1",
            description = "This is a test beer 1",
            year = "2023",
            abv = 5.0,
            image = "test_image_1.jpg",
            insertedBy = null
        )
        val beer2 = Beer(
            beerId = 2,
            title = "Test Beer 2",
            description = "This is a test beer 2",
            year = "2023",
            abv = 6.0,
            image = "test_image_2.jpg",
            insertedBy = null
        )

        // Insertar cervezas en la base de datos
        beerDao.insert(beer1)
        beerDao.insert(beer2)

        // Obtener todas las cervezas y observar los cambios con LiveData
        val allBeers = beerDao.getAll()

        // Crear un Observer para escuchar los cambios
        val observer = Observer<List<Beer>> {
            // Verificar que las cervezas insertadas están presentes en la lista
            assertEquals(listOf(beer1, beer2), it)
            assertTrue(it.size == 2)
            assertTrue(it.contains(beer1))
            assertTrue(it.contains(beer2))
        }

        // Observar el LiveData
        allBeers.observeForever(observer)

        // Esperar un momento para que los cambios se propaguen
        // Esto es importante ya que las operaciones en la base de datos pueden no ser instantáneas
        kotlinx.coroutines.delay(1000)

        // Dejar de observar para evitar problemas de memoria
        allBeers.removeObserver(observer)
    }
    @Test
    fun insertAndDeleteBeer() = runBlocking {
        // Crear cerveza de prueba
        val beer = Beer(
            beerId = 1,
            title = "Test Beer",
            description = "This is a test beer",
            year = "2023",
            abv = 5.0,
            image = "test_image.jpg",
            insertedBy = null
        )

        // Insertar cerveza en la base de datos
        beerDao.insert(beer)

        // Eliminar la cerveza de la base de datos
        beerDao.delete(beer)

        // Obtener todas las cervezas y observar los cambios con LiveData
        val allBeers = beerDao.getAll()

        // Verificar que la lista está vacía después de eliminar la cerveza
        assertTrue(allBeers.value.isNullOrEmpty())
    }
}
