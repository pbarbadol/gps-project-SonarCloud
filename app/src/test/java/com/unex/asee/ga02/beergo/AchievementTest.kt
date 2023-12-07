import com.unex.asee.ga02.beergo.model.Achievement
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class AchievementTest {

    private lateinit var achievement: Achievement

    @Before
    fun init() {
        achievement = Achievement(
            achievementId = 1,
            title = "Sample Achievement",
            description = "A description of the achievement",
            expPoint = 10,
            category = "Sample Category"
        )
    }

    @Test
    fun getAchievementId() {
        assertEquals(1, achievement.achievementId)
    }

    @Test
    fun getTitle() {
        assertEquals("Sample Achievement", achievement.title)
    }

    @Test
    fun getDescription() {
        assertEquals("A description of the achievement", achievement.description)
    }

    @Test
    fun getExpPoint() {
        assertEquals(10, achievement.expPoint)
    }

    @Test
    fun getCategory() {
        assertEquals("Sample Category", achievement.category)
    }
}
