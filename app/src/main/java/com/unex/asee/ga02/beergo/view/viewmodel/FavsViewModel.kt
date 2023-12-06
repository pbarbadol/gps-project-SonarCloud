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

class FavsViewModel(
    private var beerRepository: BeerRepository,
    private var userRepository: UserRepository,
    private var favRepository: FavRepository
): ViewModel() {

    /**
     * Cambia el valor del LiveData de la cerveza seleccionada a null.
     * @return Método setSelecterBeer de beerRepository.
     */
    fun setNoSelectedBeer(){
        beerRepository.setSelectedBeer(null)
    }

    /**
     * Cambia el valor del LiveData de la cerveza seleccionada.
     *
     * @param beer la Cerveza seleccionada
     * @return Método setSelecterBeer de beerRepository.
     */
    fun setSelectedBeer(beer: Beer){
        beerRepository.setSelectedBeer(beer)
    }

    /**
     * Obtiene la cerveza seleccionada actualmente.
     * @return @return Método getSelecterBeer de beerRepository..
     */
    fun getSelectedBeer(): Beer? {
        return beerRepository.getSelectedBeer()
    }

    /**
     * Obtiene el usuario logueado.
     * @return @return Método getCurrentUser de userRepository.
     */
    fun getCurrentUser(): User? {
        return userRepository.getCurrentUser()
    }

    /**
     * Obtiene las cervezas favoritas de un usuario
     *
     * @param userId el ID del usuario
     * @return Método loadFavs de favRepository.
     */
    suspend fun loadFavs(userId: Long): List<Beer>{
        return favRepository.loadFavs(userId)
    }

    /**
     * Borra una de las cervezas favoritas de un usuario
     *
     * @param userId el ID del usuario
     * @param beerId el ID de la cerveza
     * @return Método deleteFav de favRepository.
     */
    suspend fun deleteFav(userId: Long, beerId: Long){
        return favRepository.deleteFav(userId, beerId)
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
                return FavsViewModel(
                    (application as BeerGoApplication).appContainer.beerRepository,
                    (application as BeerGoApplication).appContainer.userRepository,
                    (application as BeerGoApplication).appContainer.favRepository
                ) as T
            }
        }
    }
}