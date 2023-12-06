import android.content.Context
import android.widget.Toast
import com.unex.asee.ga02.beergo.data.beerAchievements
import com.unex.asee.ga02.beergo.database.BeerGoDatabase
import com.unex.asee.ga02.beergo.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Clase para verificar y otorgar logros al usuario.
 *
 * @property context Contexto de la aplicación.
 * @property user Usuario actual.
 */
class CheckAchievement(private val context: Context, private val user: User) {
    private var db: BeerGoDatabase = BeerGoDatabase.getInstance(context)!!

    /**
     * Verifica y otorga el logro "First Sip" si el usuario ha añadido la primera cerveza a su colección.
     */
    suspend fun checkFirstSipAchievement() {
        val cont = db.userDao().countBeersInsertedByUser(user.userId)
        if (cont > 0) {
            insertAchievementAndShowToast(0)
        }
    }

    /**
     * Verifica y otorga el logro "Eclectic Taste" si el usuario ha añadido al menos 1 cerveza a favoritos.
     */
    suspend fun checkEclecticTasteAchievement() {
        val cont = db.userDao().countBeersInsertedByUser(user.userId)
        if (cont > 0) {
            insertAchievementAndShowToast(1)
        }
    }

    /**
     * Verifica y otorga el logro "Aromatic Words" si el usuario ha añadido al menos 1 comentario a una cerveza.
     */
    suspend fun checkAromaticWordsAchievement() {
        val numComments = db.userDao().getNumberComments(user.userId)
        if (numComments > 0) {
            insertAchievementAndShowToast(2)
        }
    }

    /**
     * Verifica y otorga el logro "Global Flavor Explorer" si el usuario ha añadido al menos 3 cervezas a favoritos.
     */
    suspend fun checkGlobalFlavorExplorerAchievement() {
        val cont = db.userDao().countBeersInsertedByUser(user.userId)
        if (cont >= 3) {
            insertAchievementAndShowToast(3)
        }
    }

    /**
     * Verifica y otorga el logro "Diverse Styles" si el usuario tiene al menos 1 cerveza con un grado de alcohol mayor a 10.
     */
    suspend fun checkDiverseStylesAchievement() {
        val beers = db.userDao().getBeersByUserId(user.userId) //TODO: arreglar repository
        val hasDiverseStyleBeer = beers.any { it.abv > 10.0 }
        if (hasDiverseStyleBeer) {
            insertAchievementAndShowToast(4)
        }
    }

    /**
     * Verifica y otorga el logro "Beer Rhythm" si el usuario ha añadido al menos 3 cervezas a su colección.
     */
    suspend fun checkBeerRhythmAchievement() {
        val cont = db.userDao().countBeersInsertedByUser(user.userId)
        if (cont >= 3) {
            insertAchievementAndShowToast(5)
        }
    }

    /**
     * Verifica y otorga el logro "Collection Master" si el usuario ha añadido al menos 5 comentarios.
     */
    suspend fun checkCollectionMasterAchievement() {
        val numComments = db.userDao().getNumberComments(user.userId)
        if (numComments >= 5) {
            insertAchievementAndShowToast(6)
        }
    }

    /**
     * Verifica y otorga el logro "Beer Party King" si el usuario ha añadido al menos 7 cervezas a su colección.
     */
    suspend fun checkBeerPartyKingAchievement() {
        val cont = db.userDao().countBeersInsertedByUser(user.userId)
        if (cont >= 7) {
            insertAchievementAndShowToast(7)
        }
    }

    /**
     * Inserta el logro en la base de datos y muestra un Toast si el logro se obtuvo por primera vez.
     *
     * @param achievementIndex Índice del logro en la lista de logros.
     */
    private suspend fun insertAchievementAndShowToast(achievementIndex: Int) {
//        withContext(Dispatchers.IO) {
//            val achievement = beerAchievements[achievementIndex]
//            val existiaAntes =
//                db.achievementDao().checkAchievement(user.userId, achievement.achievementId) > 0
//            if (!existiaAntes) {
//                db.achievementDao().insertAndRelate(achievement, user.userId)
//                withContext(Dispatchers.Main) {
//                    Toast.makeText(
//                        context,
//                        "Logro desbloqueado: ${achievement.title}",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//        } TODO: no funciona porque se ha eliminado el metodo checkAchievement debido a que ahora se hace el check desde repository. Hay que arreglarlo
    }
}


