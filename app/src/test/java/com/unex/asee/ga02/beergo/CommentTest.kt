import com.unex.asee.ga02.beergo.model.Beer
import com.unex.asee.ga02.beergo.model.Comment
import com.unex.asee.ga02.beergo.model.User
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class CommentTest {

    private lateinit var user: User
    private lateinit var beer: Beer
    private lateinit var comment: Comment

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
        comment = Comment(
            commentId = 1,
            beerId = beer.beerId,
            userId = user.userId,
            comment = "This is a test comment",
            userName = user.name
        )
    }

    @Test
    fun getCommentId() {
        assertEquals(1, comment.commentId)
    }

    @Test
    fun getBeerId() {
        assertEquals(beer.beerId, comment.beerId)
    }

    @Test
    fun getUserId() {
        assertEquals(user.userId, comment.userId)
    }

    @Test
    fun getComment() {
        assertEquals("This is a test comment", comment.comment)
    }

    @Test
    fun getUserName() {
        assertEquals(user.name, comment.userName)
    }
}
