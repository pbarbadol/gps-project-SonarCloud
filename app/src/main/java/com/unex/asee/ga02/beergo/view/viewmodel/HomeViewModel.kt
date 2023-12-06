package com.unex.asee.ga02.beergo.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.unex.asee.ga02.beergo.BeerGoApplication
import com.unex.asee.ga02.beergo.model.Beer
import com.unex.asee.ga02.beergo.model.User
import com.unex.asee.ga02.beergo.repository.BeerRepository
import com.unex.asee.ga02.beergo.repository.UserRepository

class HomeViewModel(
    private var userRepository: UserRepository,
    private var beerRepository: BeerRepository
): ViewModel() {

    /**
     * Cambia el valor del LiveData de la cerveza seleccionada.
     *
     * @param beer la Cerveza seleccionada
     * @return Método setSelecterBeer de beerRepository.
     */
    fun setSelectedBeer(beer: Beer?){ //TODO: estamos almacenando el valor en el repositorio. ¿Es correcto?
        return beerRepository.setSelectedBeer(beer)
    }

    /**
     * Guarda la información del usuario logueado.
     *
     * @param user el usuario
     * @return @return Método setUser de userRepository.
     */
    fun setUser(user: User?) {
        return userRepository.setUser(user)
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
                return HomeViewModel(
                    (application as BeerGoApplication).appContainer.userRepository,
                    (application as BeerGoApplication).appContainer.beerRepository
                ) as T
            }
        }
    }
}