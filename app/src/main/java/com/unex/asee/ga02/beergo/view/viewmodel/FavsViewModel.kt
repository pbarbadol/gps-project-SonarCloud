package com.unex.asee.ga02.beergo.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.unex.asee.ga02.beergo.BeerGoApplication
import com.unex.asee.ga02.beergo.model.Beer
import com.unex.asee.ga02.beergo.model.User
import com.unex.asee.ga02.beergo.repository.FavRepository
import kotlinx.coroutines.launch

class FavsViewModel(
    private var favRepository: FavRepository
): ViewModel() {

    var user: User? = null
        set(value) {
            field = value
        }

    private val _spinner = MutableLiveData<Boolean>(false)
    val spinner: LiveData<Boolean>
        get() = _spinner
    var beer: Beer? = null
        set(value) {
            field = value
        }

    private val _toast = MutableLiveData<String?>()
    val toast: LiveData<String?>
        get() = _toast

    var favBeers: LiveData<List<Beer>> = MutableLiveData<List<Beer>>()
    init{
        favBeers = favRepository.loadFavs(user!!.userId)
    }


/*
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
     * Obtiene las cervezas favoritas de un usuario
     *
     * @param userId el ID del usuario
     * @return Método loadFavs de favRepository.
     */
    suspend fun loadFavs(userId: Long): List<Beer>{
        return favRepository.loadFavs(userId)
    }


    suspend fun deleteFav(userId: Long, beerId: Long){
        return favRepository.deleteFav(userId, beerId)
    }*/

    fun loadFavourites(){
        viewModelScope.launch {
            _spinner.value = true
            favBeers = favRepository.loadFavs(user!!.userId)
            _spinner.value = false
        }
    }
    fun onToastShown() {
        _toast.value = null
    }

    fun deleteBeer(beer: Beer) {
        viewModelScope.launch {
            favRepository.deleteFav(user!!.userId, beer.beerId)
            _toast.value = "${beer.title} eliminada correctamente de favoritos"
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
                return FavsViewModel(
                    (application as BeerGoApplication).appContainer.favRepository
                ) as T
            }
        }
    }
}