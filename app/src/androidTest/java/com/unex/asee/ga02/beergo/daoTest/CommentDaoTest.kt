import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.unex.asee.ga02.beergo.database.BeerGoDatabase
import com.unex.asee.ga02.beergo.database.CommentDao
import com.unex.asee.ga02.beergo.model.Beer
import com.unex.asee.ga02.beergo.model.Comment
import com.unex.asee.ga02.beergo.model.User
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CommentDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var commentDao: CommentDao
    private lateinit var db: BeerGoDatabase

    @Before
    fun createDb() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            BeerGoDatabase::class.java
        ).allowMainThreadQueries().build()

        commentDao = db.commentDao()
    }

    @After
    fun closeDb() {
        db.close()
        db.clearAllTables()

    }

    @Test
    fun insertAndRetrieveComment() = runBlocking {
        // Crear un usuario y una cerveza de prueba
        val user = User(name = "TestUser", password = "TestPassword")
        val userId = db.userDao().insert(user)
        user.userId = userId

        val beer = Beer(
            beerId = 1,
            title = "TestBeer",
            description = "TestDescription",
            year = "TestYear",
            abv = 5.0,
            image = "TestImage",
            insertedBy = userId
        )
        db.beerDao().insert(beer)

        // Crear un comentario de prueba
        val comment = Comment(
            commentId = 1,
            beerId = beer.beerId,
            userId = userId,
            comment = "TestComment",
            userName = "TestUser"
        )

        // Insertar el comentario en la base de datos
        commentDao.insert(comment)

        // Recuperar el comentario por ID
        val retrievedComment = commentDao.findById(comment.commentId)

        // Verificar que el comentario recuperado sea igual al comentario insertado
        assertNotNull(retrievedComment)
        assertEquals(comment, retrievedComment)
    }

    @Test
    fun insertAndDeleteComment() = runBlocking {
        // Crear un usuario y una cerveza de prueba
        val user = User(name = "TestUser", password = "TestPassword")
        val userId = db.userDao().insert(user)

        val beer = Beer(
            beerId = 1,
            title = "TestBeer",
            description = "TestDescription",
            year = "TestYear",
            abv = 5.0,
            image = "TestImage",
            insertedBy = userId
        )
        db.beerDao().insert(beer)

        // Crear un comentario de prueba
        val comment = Comment(
            commentId = 1,
            beerId = beer.beerId,
            userId = userId,
            comment = "TestComment",
            userName = "TestUser"
        )

        // Insertar el comentario en la base de datos
        commentDao.insert(comment)

        // Eliminar el comentario de la base de datos
        commentDao.delete(comment)

        // Recuperar el comentario por ID después de eliminarlo
        val retrievedComment = commentDao.findById(comment.commentId)

        // Verificar que el comentario no existe después de eliminarlo
        assertNull(retrievedComment)
    }

    @Test
    fun findByBeerId() = runBlocking {
        // Crear un usuario y una cerveza de prueba
        val user = User(name = "TestUser", password = "TestPassword")
        val userId = db.userDao().insert(user)
        user.userId = userId
        val beer = Beer(
            beerId = 1,
            title = "TestBeer",
            description = "TestDescription",
            year = "TestYear",
            abv = 5.0,
            image = "TestImage",
            insertedBy = userId
        )
        db.beerDao().insert(beer)

        // Crear comentarios de prueba
        val comment1 = Comment(
            commentId = 1,
            beerId = beer.beerId,
            userId = user.userId,
            comment = "TestComment1",
            userName = "TestUser"
        )
        val comment2 = Comment(
            commentId = 2,
            beerId = beer.beerId,
            userId = user.userId,
            comment = "TestComment2",
            userName = "TestUser"
        )

        // Insertar los comentarios en la base de datos
        commentDao.insert(comment1)
        commentDao.insert(comment2)

        // Recuperar los comentarios por el ID de la cerveza
        val commentsByBeer = commentDao.findByBeer(beer.beerId)


        commentsByBeer.observeForever { comments ->
            // Verificar que la lista de comentarios no sea nula y contenga los comentarios insertados
            assertNotNull(comments)
            assertEquals(2, comments.size)
            assertEquals(comment1, comments[0])
            assertEquals(comment2, comments[1])
        }
    }
}
