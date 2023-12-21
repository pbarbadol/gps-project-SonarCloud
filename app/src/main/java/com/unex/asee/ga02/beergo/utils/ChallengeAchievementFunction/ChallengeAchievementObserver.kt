//package com.unex.asee.ga02.beergo.utils.ChallengeAchievementFunction
//
//import CheckAchievement
//import android.content.Context
//import android.widget.Toast
//import com.unex.asee.ga02.beergo.database.BeerGoDatabase
//import com.unex.asee.ga02.beergo.model.User
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.MainScope
//import kotlinx.coroutines.launch
//
//
//class ChallengeAchievementObserver(
//    private val user: User, private val context: Context, private val db: BeerGoDatabase
//) : DatabaseObserver, CoroutineScope by MainScope() {
//    private val checkAchievement = CheckAchievement(context, user)
//
//
//    override fun onTableChanged(tableName: String) {
//        when (tableName) {
//            "UserBeerCrossRef" -> {
//                launch(Dispatchers.IO) {
//                    checkAchievement.checkFirstSipAchievement()
//                    checkAchievement.checkDiverseStylesAchievement()
//                    checkAchievement.checkBeerRhythmAchievement()
//                    checkAchievement.checkBeerPartyKingAchievement()
//                }
//            }
//
//            "Comment" -> {
//                // Lógica para comprobar si se ha desbloqueado el logro
//                launch(Dispatchers.IO) {
//                    checkAchievement.checkAromaticWordsAchievement()
//                    checkAchievement.checkCollectionMasterAchievement()
//                }
//            }
//
//            "UserFavouriteBeerCrossRef" -> {
//                // Lógica para comprobar si se ha desbloqueado el logro
//                launch(Dispatchers.IO) {
//                    checkAchievement.checkEclecticTasteAchievement()
//                    checkAchievement.checkGlobalFlavorExplorerAchievement()
//                }
//            }
//        }
//    }
//
//    // Función para mostrar un Toast
//    private fun showToast(message: String) {
//        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
//    }
//}