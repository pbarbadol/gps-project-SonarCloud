package com.unex.asee.ga02.beergo.view.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.unex.asee.ga02.beergo.model.Achievement

//Aquí comprobamos los logros del usuario
//Y se llama esta función desde los viewModels
//Esos viewModels observan el livedata de esta clase
class CheckViewModel {
    private val _userAchivements = MutableLiveData<List<Achievement>>(null)
    val user: LiveData<List<Achievement>>
        get() = _userAchivements
    var userAchievemntInSession: List<Achievement>? = null
        set(value) {
            field = value
            _userAchivements.value = value!!
            Log.d("Observation", "UserInSession updated: $value")
        }
}