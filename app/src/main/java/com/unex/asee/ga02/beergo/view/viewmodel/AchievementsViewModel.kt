package com.unex.asee.ga02.beergo.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.unex.asee.ga02.beergo.BeerGoApplication
import com.unex.asee.ga02.beergo.model.Achievement
import com.unex.asee.ga02.beergo.model.User
import com.unex.asee.ga02.beergo.model.UserWithAchievements
import com.unex.asee.ga02.beergo.repository.AchievementRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AchievementsViewModel(
    private var achievementRepository: AchievementRepository
): ViewModel() {

    val achievementsUser = achievementRepository.listAchievementUser
    val achievements = achievementRepository.getAllAchievements()
    var user: User? = null
        set(value) {
            field = value
            achievementRepository.setUserid(value!!.userId)

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