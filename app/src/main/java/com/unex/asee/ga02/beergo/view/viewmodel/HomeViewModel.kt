package com.unex.asee.ga02.beergo.view.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    // Propiedad pública solo de lectura para acceder al LiveData del usuario.
    private val _user = MutableLiveData<User>(null)
    private val _beer = MutableLiveData<Beer>(null)
    val user: LiveData<User>
        get() = _user
    var userInSession: User? = null
        set(value) {
            field = value
            _user.value = value!!
            Log.d("Observation", "UserInSession updated: $value")
        }

    val beer: LiveData<Beer>
        get() = _beer
    var beerInSession: Beer? = null
        set(value) {
            field = value
            _beer.value = value!!
        }

    fun setBeerNull() {
        _beer.value = null
    }

    fun isNull(): Boolean {
        return (beerInSession == null)
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