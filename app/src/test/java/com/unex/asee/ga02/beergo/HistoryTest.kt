import com.unex.asee.ga02.beergo.model.Beer
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.Date

class HistoryTest {

    private lateinit var beer: Beer
    private lateinit var history: History

    @Before
    fun init() {
        beer = Beer(
            beerId = 1,
            title = "Sample Beer",
            description = "A description of the beer",
            year = "2022",
            abv = 5.0,
            image = "beer_image.jpg",
            insertedBy = null
        )
        history = History(beer = beer, date = Date())
    }

    @Test
    fun beerProperty() {
        assertEquals(beer, history.beer)
    }

    @Test
    fun beerPropertyAfterModification() {
        val newBeer = Beer(
            beerId = 2,
            title = "New Beer",
            description = "A new beer description",
            year = "2023",
            abv = 6.0,
            image = "new_beer_image.jpg",
            insertedBy = null
        )
        history.beer = newBeer
        assertEquals(newBeer, history.beer)
    }

    @Test
    fun dateProperty() {
        assertEquals(Date::class, history.date::class)
    }

    @Test
    fun datePropertyAfterModification() {
        val newDate = Date()
        history.date = newDate
        assertEquals(newDate, history.date)
    }

    @Test
    fun saveHistory() {
        val historyToSave = History(
            beer = beer,
            date = Date()
        )
        History.saveHistory(historyToSave)
        val historyList = History.getHistoryList()
        assertEquals(1, historyList.size)
        assertEquals(historyToSave, historyList[0])
    }
}
