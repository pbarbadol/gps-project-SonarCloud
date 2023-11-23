package com.unex.asee.ga02.beergo.view.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.unex.asee.ga02.beergo.model.Beer

/**
 * ViewModel para gestionar la información de las cervezas en la aplicación.
 */
class BeerViewModel : ViewModel() {

    // LiveData que contiene información de la cerveza seleccionada.
    private val selectedBeer = MutableLiveData<Beer>()

    /**
     * Propiedad pública solo de lectura para acceder al LiveData de la cerveza seleccionada.
     */
    val beer: LiveData<Beer>
        get() = selectedBeer

    /**
     * Cambia el valor del LiveData de la cerveza seleccionada.
     * @param beer La cerveza que se establecerá como la cerveza seleccionada.
     */
    fun setSelectedBeer(beer: Beer?) {
        selectedBeer.value = beer
        Log.d("BeerViewModel", "Cerveza seleccionada: $beer")
    }

    /**
     * Obtiene la cerveza seleccionada actualmente.
     * @return La cerveza seleccionada. Puede ser nulo si no hay una cerveza seleccionada.
     */
    fun getSelectedBeer(): Beer? {
        return selectedBeer.value
    }
}
