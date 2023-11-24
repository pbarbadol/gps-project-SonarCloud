import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.unex.asee.ga02.beergo.data.beerAchievements
import com.unex.asee.ga02.beergo.database.BeerGoDatabase
import com.unex.asee.ga02.beergo.model.User
import com.unex.asee.ga02.beergo.view.home.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CheckAchievement(private val context: Context,private val viewModelStoreOwner: ViewModelStoreOwner) {
    private lateinit var userViewModel: UserViewModel
    private lateinit var currentUser: User
    private lateinit var db: BeerGoDatabase
init {
    db = BeerGoDatabase.getInstance(context)!!
}

     suspend fun checkAchievements() {
        userViewModel = ViewModelProvider(viewModelStoreOwner).get(UserViewModel::class.java)

        // Obtener el usuario actual
        currentUser = userViewModel.getUser()

        // Obtener la lista de logros
        val achievements = db.achievementDao().getAll()

        for (achievement in achievements) {
            when (achievement.achievementId) {
                1L -> checkFirstSipAchievement()
                2L -> checkEclecticTasteAchievement()
                3L -> checkAromaticWordsAchievement()
                4L -> checkGlobalFlavorExplorerAchievement()
                5L -> checkDiverseStylesAchievement()
                6L -> checkBeerRhythmAchievement()
                7L -> checkCollectionMasterAchievement()
                8L -> checkBeerPartyKingAchievement()
                // ... Agrega más casos según el ID de cada logro
            }
        }
    }

    private suspend fun checkFirstSipAchievement() {
        // Verifica si el usuario ha añadido la primera cerveza a su colección
        val cont = db.beerDao().getNumberBeersFromAllUser()
        if (cont > 0) {
            // Cumple con el logro
            // Puedes actualizar la base de datos o realizar otras acciones
            // También puedes mostrar una notificación al usuario
            Toast.makeText(context, "¡Logro desbloqueado!", Toast.LENGTH_SHORT).show()
            db.achievementDao().insertAndRelate(beerAchievements[0], currentUser.userId)
        }
    }

    private suspend fun checkEclecticTasteAchievement() {
        // Verifica si el usuario ha añadido al menos 1 cerveza a favoritos
        val cont = db.beerDao().BeerInFavorites(currentUser.userId)
        if (cont > 0) {
            // Cumple con el logro
            // Puedes actualizar la base de datos o realizar otras acciones
            // También puedes mostrar una notificación al usuario
            Toast.makeText(context, "¡Logro desbloqueado!", Toast.LENGTH_SHORT).show()
            db.achievementDao().insertAndRelate(beerAchievements[1], currentUser.userId)
        }
    }

    private suspend fun checkAromaticWordsAchievement() {
        // Verifica si el usuario ha añadido al menos 1 comentario a una cerveza
        val numComments = db.beerDao().getNumberComments(currentUser.userId)
        if (numComments > 0) {
            // Cumple con el logro
            // Puedes actualizar la base de datos o realizar otras acciones
            // También puedes mostrar una notificación al usuario
            Toast.makeText(context, "¡Logro desbloqueado!", Toast.LENGTH_SHORT).show()
            db.achievementDao().insertAndRelate(beerAchievements[2], currentUser.userId)
        }
    }

    private suspend fun checkGlobalFlavorExplorerAchievement() {
        // Verifica si el usuario ha añadido al menos 3 cervezas a favoritos
        val cont = db.beerDao().BeerInFavorites(currentUser.userId)
        if (cont >= 3) {
            // Cumple con el logro
            // Puedes actualizar la base de datos o realizar otras acciones
            // También puedes mostrar una notificación al usuario
            Toast.makeText(context, "¡Logro desbloqueado!", Toast.LENGTH_SHORT).show()
            db.achievementDao().insertAndRelate(beerAchievements[3], currentUser.userId)
        }
    }

    private suspend fun checkDiverseStylesAchievement() {
        // Verifica si el usuario tiene al menos 1 cerveza con un grado de alcohol mayor a 10
        val beers = db.beerDao().getBeersFromAllUser()
        val hasDiverseStyleBeer = beers.any { it.abv > 10.0 }
        if (hasDiverseStyleBeer) {
            // Cumple con el logro
            // Puedes actualizar la base de datos o realizar otras acciones
            // También puedes mostrar una notificación al usuario
            Toast.makeText(context, "¡Logro desbloqueado!", Toast.LENGTH_SHORT).show()
            db.achievementDao().insertAndRelate(beerAchievements[4], currentUser.userId)
        }
    }

    private suspend fun checkBeerRhythmAchievement() {
        // Verifica si el usuario ha añadido al menos 3 cervezas a su colección
        val cont = db.beerDao().getNumberBeersFromAllUser()
        if (cont >= 3) {
            // Cumple con el logro
            // Puedes actualizar la base de datos o realizar otras acciones
            // También puedes mostrar una notificación al usuario
            Toast.makeText(context, "¡Logro desbloqueado!", Toast.LENGTH_SHORT).show()
            db.achievementDao().insertAndRelate(beerAchievements[5], currentUser.userId)
        }
    }

    private suspend fun checkCollectionMasterAchievement() {
        // Verifica si el usuario ha añadido al menos 5 comentarios
        val numComments = db.beerDao().getNumberComments(currentUser.userId)
        if (numComments >= 5) {
            // Cumple con el logro
            // Puedes actualizar la base de datos o realizar otras acciones
            // También puedes mostrar una notificación al usuario
            Toast.makeText(context, "¡Logro desbloqueado!", Toast.LENGTH_SHORT).show()
            db.achievementDao().insertAndRelate(beerAchievements[6], currentUser.userId)
        }
    }

    private suspend fun checkBeerPartyKingAchievement() {
        // Verifica si el usuario ha añadido al menos 7 cervezas a su colección
        val cont = db.beerDao().getNumberBeersFromAllUser()
        if (cont >= 7) {
            // Cumple con el logro
            // Puedes actualizar la base de datos o realizar otras acciones
            // También puedes mostrar una notificación al usuario
            Toast.makeText(context, "¡Logro desbloqueado!", Toast.LENGTH_SHORT).show()
            db.achievementDao().insertAndRelate(beerAchievements[7], currentUser.userId)
        }
    }
}


