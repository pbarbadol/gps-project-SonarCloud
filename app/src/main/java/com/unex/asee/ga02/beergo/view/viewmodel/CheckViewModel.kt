package com.unex.asee.ga02.beergo.view.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.unex.asee.ga02.beergo.BeerGoApplication
import com.unex.asee.ga02.beergo.data.beerAchievements
import com.unex.asee.ga02.beergo.model.Achievement
import com.unex.asee.ga02.beergo.model.User
import com.unex.asee.ga02.beergo.repository.AchievementRepository
import com.unex.asee.ga02.beergo.repository.FavRepository
import com.unex.asee.ga02.beergo.repository.UserRepository
import kotlinx.coroutines.launch

//Aquí comprobamos los logros del usuario
//Y se llama esta función desde los viewModels
//Esos viewModels observan el livedata de esta clase
class CheckViewModel(
    private var userRepository: UserRepository,
    private var achievementRepository: AchievementRepository
): ViewModel() {
    val listAchievement = mutableListOf<Achievement>()
    var bool = MutableLiveData<Boolean>(false)
    var user: User? = null
        set(value) {
            field = value
        }

    private val _toast = MutableLiveData<String?>()
    val toast: LiveData<String?>
        get() = _toast

    private val _toast2 = MutableLiveData<String?>()
    val toast2: LiveData<String?>
        get() = _toast2
    fun onToastShown(){
        _toast.value = null
    }
    fun onToastShown2(){
        _toast2.value = null
    }

    /*


    private val _userAchivements = MutableLiveData<List<Achievement>>(null)
    val user: LiveData<List<Achievement>>
        get() = _userAchivements
    var userAchievemntInSession: List<Achievement>? = null
        set(value) {
            field = value
            _userAchivements.value = value!!
            Log.d("Observation", "UserInSession updated: $value")
        }*/
    fun checkAchievementsInsert(){
        checkFirstSipAchievement()
        checkDiverseStylesAchievement()
        checkBeerRhythmAchievement()
        checkBeerPartyKingAchievement()

    }

    fun checkAchievementsFav(){
        checkEclecticTasteAchievement()
        checkGlobalFlavorExplorerAchievement()

    }

    fun checkAchievementsComment(){
        checkAromaticWordsAchievement()
        checkCollectionMasterAchievement()
    }
    fun checkFirstSipAchievement() {
        userRepository.countBeersInsertedByUser(user!!.userId).observeForever { count ->
            Log.d("Observation", "Count: $count")
            if (count != null && count > 0) {
                insertAchievementAndShowToast(0L)
            }
        }
    }


    /**
     * Verifica y otorga el logro "Eclectic Taste" si el usuario ha añadido al menos 1 cerveza a favoritos.
     */
    fun checkEclecticTasteAchievement() {
            userRepository.countFavouritesByUser(user!!.userId).observeForever { count ->
                if (count != null && count > 0) {
                    insertAchievementAndShowToast(1L)
                }
            }
        }


        /**
         * Verifica y otorga el logro "Aromatic Words" si el usuario ha añadido al menos 1 comentario a una cerveza.
         */
        fun checkAromaticWordsAchievement() {
                userRepository.countCommentsByUser(user!!.userId).observeForever { numComments ->
                    Log.d("Observation", "CountComment: $numComments")
                    if (numComments != null && numComments >0) {
                        Log.d("Observation", "CountComment INSERTADO: $numComments")
                        insertAchievementAndShowToast(2L)
                    }
                }
        }

        /**
         * Verifica y otorga el logro "Global Flavor Explorer" si el usuario ha añadido al menos 3 cervezas a favoritos.
         */
        fun checkGlobalFlavorExplorerAchievement() {
            userRepository.countFavouritesByUser(user!!.userId).observeForever { count ->
                if (count != null && count >= 3) {
                    insertAchievementAndShowToast(3L)
                }
            }


        }

        /**
         * Verifica y otorga el logro "Diverse Styles" si el usuario tiene al menos 1 cerveza con un grado de alcohol mayor a 10.
         */
        fun checkDiverseStylesAchievement() {
            userRepository.getUserBeers(user!!.userId).observeForever { beers ->
                if (beers != null && beers.isNotEmpty() && beers.any { it.abv > 10.0 }) {
                    insertAchievementAndShowToast(4L)
                }
            }
        }


        /**
         * Verifica y otorga el logro "Beer Rhythm" si el usuario ha añadido al menos 3 cervezas a su colección.
         */
        fun checkBeerRhythmAchievement() {
            userRepository.countBeersInsertedByUser(user!!.userId).observeForever { count ->
                Log.d("Observation", "Count: $count")
                if (count != null && count >= 3) {
                    insertAchievementAndShowToast(5L)
                }
            }
        }

        /**
         * Verifica y otorga el logro "Collection Master" si el usuario ha añadido al menos 5 comentarios.
         */
        fun checkCollectionMasterAchievement() {
            userRepository.countCommentsByUser(user!!.userId).observeForever { numComments ->
                if (numComments != null && numComments >= 5) {
                    insertAchievementAndShowToast(6L)
                }
            }
        }


        /**
         * Verifica y otorga el logro "Beer Party King" si el usuario ha añadido al menos 7 cervezas a su colección.
         */
        fun checkBeerPartyKingAchievement() {
            userRepository.countBeersInsertedByUser(user!!.userId).observeForever { count ->
                Log.d("Observation", "Count: $count")
                if (count != null && count >= 7) {
                    insertAchievementAndShowToast(7L)
                }
            }
        }
    fun insertAchievementAndShowToast(achievementIndex: Long) {
        // Se obtiene la lista de logros actual
        bool.value = false
        Log.d("Observation", "Se insertará")


        if (!listAchievement.contains(beerAchievements[achievementIndex.toInt()]) ) {
            if (achievementIndex == 2L || achievementIndex == 6L) {
                _toast2.value =
                    "¡Has desbloqueado el logro ${beerAchievements[achievementIndex.toInt()].title}!"
            }
            Log.d("Observation", "Se insertará dentro if")
            _toast.value =
                "¡Has desbloqueado el logro ${beerAchievements[achievementIndex.toInt()].title}!"

            viewModelScope.launch {
                val achievement = beerAchievements[achievementIndex.toInt()]
                achievementRepository.insertAndRelate(achievement, user!!.userId)
            }

            listAchievement.add(beerAchievements[achievementIndex.toInt()])


            bool.value = true
        }
    }



    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T { // Get the Application object from extras

                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                return CheckViewModel(
                    (application as BeerGoApplication).appContainer.userRepository,
                    (application as BeerGoApplication).appContainer.achievmentRepository
                ) as T
            }
        }
    }

}