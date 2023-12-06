package com.unex.asee.ga02.beergo.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.unex.asee.ga02.beergo.BeerGoApplication
import com.unex.asee.ga02.beergo.model.Beer
import com.unex.asee.ga02.beergo.model.User
import com.unex.asee.ga02.beergo.repository.BeerRepository
import com.unex.asee.ga02.beergo.repository.UserRepository

class InsertBeerViewModel(
    private var userRepository: UserRepository,
    private var beerRepository: BeerRepository
): ViewModel() {

    /**
     * Obtiene el usuario logueado
     *
     * @return Método getCurrentUser de userRepository.
     */
    fun getCurrentUser(): User {
        return userRepository.getCurrentUser()
    }

    /**
     *  Añade una cerveza a la base de datos local.
     *
     *  @param beer la Cerveza que se insertará.
     *  @return Método addBeer de beerRepository.
     */
    suspend fun addBeer (beer: Beer){
        return beerRepository.addBeer(beer)
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
                return InsertBeerViewModel(
                    (application as BeerGoApplication).appContainer.userRepository,
                    (application as BeerGoApplication).appContainer.beerRepository
                ) as T
            }
        }
    }
}