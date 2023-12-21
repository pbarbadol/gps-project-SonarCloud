import com.unex.asee.ga02.beergo.model.Beer
import com.unex.asee.ga02.beergo.model.User
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class BeerTest {

    private lateinit var user: User
    private lateinit var beer: Beer

    @Before
    fun init() {
        user = User(userId = 1, name = "TestUser", password = "testpassword")
        beer = Beer(
            beerId = 1,
            title = "Sample Beer",
            description = "A description of the beer",
            year = "2022",
            abv = 5.0,
            image = "beer_image.jpg",
            insertedBy = user.userId
        )
    }

    @Test
    fun getBeerId() {
        assertEquals(1, beer.beerId)
    }

    @Test
    fun getTitle() {
        assertEquals("Sample Beer", beer.title)
    }

    @Test
    fun getDescription() {
        assertEquals("A description of the beer", beer.description)
    }

    @Test
    fun getYear() {
        assertEquals("2022", beer.year)
    }

    @Test
    fun getAbv() {
        assertEquals(5.0, beer.abv)
    }

    @Test
    fun getImage() {
        assertEquals("beer_image.jpg", beer.image)
    }

    @Test
    fun getInsertedBy() {
        assertEquals(user.userId, beer.insertedBy)
    }

}
