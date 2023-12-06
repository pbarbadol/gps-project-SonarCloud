package com.unex.asee.ga02.beergo.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.unex.asee.ga02.beergo.BeerGoApplication
import com.unex.asee.ga02.beergo.model.User
import com.unex.asee.ga02.beergo.repository.UserRepository
import com.unex.asee.ga02.beergo.utils.CredentialCheck

class JoinViewModel(
    private var userRepository: UserRepository
): ViewModel() {

    /**
     * Registra a un nuevo usuario en la base de datos.
     *
     * @param username Nombre de usuario del nuevo usuario.
     * @param password Contraseña del nuevo usuario.
     * @return Método registerUser de userRepository.
     */
    suspend fun registerUser(username: String, password: String): User? {
        return userRepository.registerUser(username, password)
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
                return JoinViewModel(
                    (application as BeerGoApplication).appContainer.userRepository
                ) as T
            }
        }
    }
}