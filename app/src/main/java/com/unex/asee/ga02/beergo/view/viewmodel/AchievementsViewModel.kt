package com.unex.asee.ga02.beergo.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.unex.asee.ga02.beergo.BeerGoApplication
import com.unex.asee.ga02.beergo.model.Achievement
import com.unex.asee.ga02.beergo.model.User
import com.unex.asee.ga02.beergo.repository.AchievementRepository

class AchievementsViewModel(
    private var achievementRepository: AchievementRepository
): ViewModel() {

    var user : User? = null

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
    fun getUserAchievements(): List<Achievement>?{
        return achievementRepository.getUserAchievements(user?.userId!!)
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
                return AchievementsViewModel(
                    (application as BeerGoApplication).appContainer.achievmentRepository
                ) as T
            }
        }
    }
}