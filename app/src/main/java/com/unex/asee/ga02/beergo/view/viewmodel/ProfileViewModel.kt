package com.unex.asee.ga02.beergo.view.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.unex.asee.ga02.beergo.BeerGoApplication
import com.unex.asee.ga02.beergo.model.Achievement
import com.unex.asee.ga02.beergo.model.User
import com.unex.asee.ga02.beergo.repository.AchievementRepository
import com.unex.asee.ga02.beergo.repository.UserRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ProfileViewModel(
    private var userRepository: UserRepository,
    private var achievementRepository: AchievementRepository
): ViewModel() {
    val achievementsUser = achievementRepository.listAchievementUser

    var user: User? = null
        set(value) {
            field = value
            achievementRepository.setUserid(value!!.userId)
            updateLevelAndExp()
            Log.d("Observation", "User: $user asignado")
        }
    var nivel: Int = 0
    var exp: Int = 0

    val _beerInsert = MutableLiveData<Int>()
     val beerInsert : LiveData<Int>
        get() = _beerInsert
    val _favInsert = MutableLiveData<Int>()
    val favInsert : LiveData<Int>
        get() = _favInsert
    val _comments =MutableLiveData<Int>()
    val comments: LiveData<Int>
        get() = _comments



    init {
        _beerInsert.value = 0
        _favInsert.value = 0
    }


    /**
     * Elimina el usuario de la base de datos.
     */
    fun deleteUser(block: suspend () -> Unit): Job {
        return viewModelScope.launch {
            if (user != null) {
                userRepository.deleteUser(user!!)
                block()
            } else
                throw Exception("User no encontrado")
        }
    }

    /**
     * Cierra la sesión del usuario.
     */
    fun cerrarSesion(block: suspend () -> Unit) : Job {
        return viewModelScope.launch {
            if (user != null) {
                //userRepository.setUser(null)
                block()
            } else
                throw Exception("User no encontrado")
        }
    }


    /**
     * Obtiene el número de cervezas insertadas por un usuario específico.
     *
     * @param userId El ID del usuario.
     * @return Método countBeersInsertedByUser de userRepository.
     */

    fun countBeersInsertedByUser() {
        userRepository.countBeersInsertedByUser(user!!.userId).observeForever{
            _beerInsert.value = it
        }
    }



    /**
     * Obtiene el número de cervezas favoritas de un usuario específico.
     *
     * @return Método countFavouritesByUser de userRepository.
     */

    fun countFavouritesByUser() {
        userRepository.countFavouritesByUser(user!!.userId).observeForever{
            _favInsert.value = it
        }
    }

    /**
     * Obtiene el número de comentarios realizados por un usuario específico.
     *
     * @return Método countCommentsByUser de userRepository.
     */

    fun countCommentsByUser(){
       userRepository.countCommentsByUser(user!!.userId).observeForever{
            _comments.value = it
        }
    }

    /**
     * Obtiene el número de logros obtenidos por un usuario específico.
     *
     * @return Método countUserAchievements de userRepository.
     */
    fun countUserAchievements(): Int {
        return if(user == null) {
            0
        }else{
            userRepository.countUserAchievements(user!!.userId)
        }
    }

    /**
     * Obtiene los logros de un usuario específico.
     *
     * @return Método getUserAchievements de achievementRepository.
     */


    private fun updateLevelAndExp() {
        achievementsUser.observeForever { userWithAchievements ->
            userWithAchievements?.let {

                for (achievement in it.achievements) {
                    exp += achievement.expPoint
                }

                while (exp >= 100) {
                    nivel++
                    exp -= 100
                }

            }
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
                return ProfileViewModel(
                    (application as BeerGoApplication).appContainer.userRepository,
                    (application as BeerGoApplication).appContainer.achievmentRepository
                ) as T
            }
        }
    }
}