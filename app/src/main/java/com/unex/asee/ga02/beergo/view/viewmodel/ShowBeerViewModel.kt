package com.unex.asee.ga02.beergo.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.unex.asee.ga02.beergo.BeerGoApplication
import com.unex.asee.ga02.beergo.model.Beer
import com.unex.asee.ga02.beergo.model.User
import com.unex.asee.ga02.beergo.repository.BeerRepository
import com.unex.asee.ga02.beergo.repository.FavRepository
import com.unex.asee.ga02.beergo.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ShowBeerViewModel(
    private val favRepository: FavRepository,
    private val beerRepository: BeerRepository,
    private val userRepository: UserRepository
): ViewModel() {

    /**
     * Comprueba si la cerveza está en la lista de favoritos del usuario
     * @return true si está en la lista de favoritos, false en caso contrario
     */
    suspend fun isInFavourite(id: Long): Boolean {
        return withContext(Dispatchers.IO) {
            val user = userRepository.getCurrentUser()
            favRepository.isFavorite(user!!.userId, id)
        }
    }

    fun getSelectedBeer(): Beer? {
        return beerRepository.getSelectedBeer()
    }

    suspend fun getUser(userId: Long) : User? {
        return userRepository.getUser(userId)
    }

    fun getCurrentUser(): User? {
        return userRepository.getCurrentUser()
    }
    suspend fun addFav(userId: Long, beerId: Long){
        favRepository.addFav(userId, beerId)
    }

    suspend fun deleteFav(userId: Long, beerId: Long){
        favRepository.deleteFav(userId, beerId)
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
                return ShowBeerViewModel(
                    (application as BeerGoApplication).appContainer.favRepository,
                    (application as BeerGoApplication).appContainer.beerRepository,
                    (application as BeerGoApplication).appContainer.userRepository,
                ) as T
            }
        }
    }
}