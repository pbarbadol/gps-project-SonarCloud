package com.unex.asee.ga02.beergo.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.unex.asee.ga02.beergo.BeerGoApplication
import com.unex.asee.ga02.beergo.model.Achievement
import com.unex.asee.ga02.beergo.model.User
import com.unex.asee.ga02.beergo.model.UserWithAchievements
import com.unex.asee.ga02.beergo.repository.AchievementRepository
import com.unex.asee.ga02.beergo.repository.UserRepository

class ProfileViewModel(
    private var userRepository: UserRepository,
    private var achievementRepository: AchievementRepository
): ViewModel() {

    /**
     * Obtiene el usuario logueado.
     *
     * @return Método getCurrentUser de userRepository.
     */
    fun getCurrentUser(): User? {
        return userRepository.getCurrentUser()
    }

    /**
     * Elimina un usuario de la base de datos.
     *
     * @param user El usuario a eliminar.
     * @return Método deleteUser de userRepository.
     */
    suspend fun deleteUser(user: User){
        return userRepository.deleteUser(user)
    }

    /**
     * Guarda la información del usuario logueado.
     *
     * @param user el usuario
     * @return @return Método setUser de userRepository.
     */
    fun setUser(user: User?){
        return userRepository.setUser(user)
    }

    /**
     * Obtiene el número de cervezas insertadas por un usuario específico.
     *
     * @param userId El ID del usuario.
     * @return Método countBeersInsertedByUser de userRepository.
     */
    suspend fun countBeersInsertedByUser(userId: Long): Int {
        return userRepository.countBeersInsertedByUser(userId)
    }

    /**
     * Obtiene el número de cervezas favoritas de un usuario específico.
     *
     * @param userId El ID del usuario.
     * @return Método countFavouritesByUser de userRepository.
     */
    suspend fun countFavouritesByUser(userId: Long): Int {
        return userRepository.countFavouritesByUser(userId)
    }

    /**
     * Obtiene el número de comentarios realizados por un usuario específico.
     *
     * @param userId El ID del usuario.
     * @return Método countCommentsByUser de userRepository.
     */
    suspend fun countCommentsByUser(userId: Long): Int {
        return userRepository.countCommentsByUser(userId)
    }

    /**
     * Obtiene el número de logros obtenidos por un usuario específico.
     *
     * @param userId El ID del usuario.
     * @return Método countUserAchievements de userRepository.
     */
    suspend fun countUserAchievements(userId: Long): Int {
        return userRepository.countUserAchievements(userId)
    }

    /**
     * Obtiene todos los logros.
     * @return Método getAllAchievements de achievementRepository.
     */
    suspend fun getAllAchievements(): List<Achievement> {
        return achievementRepository.getAllAchievements()
    }

    /**
     * Obtiene los logros de un usuario específico.
     *
     * @param userId ID del usuario.
     * @return Método getUserAchievements de achievementRepository.
     */
    suspend fun getUserAchievements(userId: Long): UserWithAchievements {
        return achievementRepository.getUserAchievements(userId)
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
                return ProfileViewModel(
                    (application as BeerGoApplication).appContainer.userRepository,
                    (application as BeerGoApplication).appContainer.achievmentRepository
                ) as T
            }
        }
    }
}