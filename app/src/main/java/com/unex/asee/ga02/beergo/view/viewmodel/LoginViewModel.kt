package com.unex.asee.ga02.beergo.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.unex.asee.ga02.beergo.BeerGoApplication
import com.unex.asee.ga02.beergo.model.User
import com.unex.asee.ga02.beergo.repository.UserRepository
import com.unex.asee.ga02.beergo.utils.CredentialCheck

class LoginViewModel(
    private var userRepository: UserRepository
): ViewModel() {

    /**
     * Intenta autenticar a un usuario utilizando el nombre de usuario y la contraseña proporcionados.
     *
     * @param username Nombre de usuario del usuario que intenta autenticarse.
     * @param password Contraseña del usuario que intenta autenticarse.
     * @return Método loginUser de userRepository.
     */
    suspend fun loginUser(username: String, password: String): User {
        return userRepository.loginUser(username, password)
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
                return LoginViewModel(
                    (application as BeerGoApplication).appContainer.userRepository
                ) as T
            }
        }
    }
}