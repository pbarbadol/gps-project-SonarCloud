package com.unex.asee.ga02.beergo.view.viewmodel

import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.unex.asee.ga02.beergo.BeerGoApplication
import com.unex.asee.ga02.beergo.api.APIError
import com.unex.asee.ga02.beergo.model.Beer
import com.unex.asee.ga02.beergo.model.User
import com.unex.asee.ga02.beergo.repository.BeerRepository
import com.unex.asee.ga02.beergo.repository.FavRepository
import com.unex.asee.ga02.beergo.repository.UserRepository
import com.unex.asee.ga02.beergo.view.list.ListAdapter
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ListViewModel (
    private val favRepository: FavRepository,
    private val beerRepository: BeerRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    var user: User? = null
    var beers: LiveData<List<Beer>>? = null
    var beersFiltered: List<Beer> = emptyList()
    var cachedBeers: List<Beer> = emptyList()

    var beer: Beer? = null
        set(value) {
            field=value
            beers = beerRepository.getAll()
        }

    private val _spinner = MutableLiveData<Boolean>()
    val spinner: LiveData<Boolean>
        get() = _spinner

    private val _toast = MutableLiveData<String?>()
    val toast: LiveData<String?>
        get() = _toast

    init {
        refresh()
    }
    private fun refresh(){
        launchDataLoad { beerRepository.tryUpdateRecentBeersCache() }
    }
    fun onToastShown(){
        _toast.value = null
    }

    /**
     * Añade una cerveza a favoritos
     *
     * @param beer la Cerveza que se añadirá a favoritos.
     */
    fun setFavourite(beer: Beer) {
        val user = userRepository.getCurrentUser()
        viewModelScope.launch {
            favRepository.addFav(user!!.userId, beer.beerId)
        }
        _toast.value = "Cerveza añadida a favoritos"
    }

    fun performSearch(query: String){
        beersFiltered = if (query.isNotBlank()) {
            cachedBeers.filter { it.title.contains(query, ignoreCase = true) }
        } else {
            cachedBeers
        }
    }


    private fun launchDataLoad(block: suspend () -> Unit) : Job {
        return viewModelScope.launch {
            try {
                _spinner.value = true
                block()
            } catch (error: APIError) {
                _toast.value = error.message
            } finally {
                _spinner.value = false
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
                return ListViewModel(
                    (application as BeerGoApplication).appContainer.favRepository,
                    (application as BeerGoApplication).appContainer.beerRepository,
                    (application as BeerGoApplication).appContainer.userRepository,
                    ) as T
            }
        }
    }
}