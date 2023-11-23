package com.unex.asee.ga02.beergo.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.unex.asee.ga02.beergo.model.User

// Definición de la clase UserModel que extiende de ViewModel.
class UserViewModel : ViewModel() {

    // LiveData que contiene información del usuario.
    private val userLiveData = MutableLiveData<User>()

    // Propiedad pública solo de lectura para acceder al LiveData del usuario.
    val user: LiveData<User>
        get() = userLiveData

    // Método para cambiar el valor del LiveData del usuario.
    fun setUser(user: User) {
        userLiveData.value = user
    }

    // Función para obtener el usuario.
    fun getUser(): User {
        return userLiveData.value!!
    }
}

