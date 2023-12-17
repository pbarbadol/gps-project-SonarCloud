package com.unex.asee.ga02.beergo.view.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.unex.asee.ga02.beergo.BeerGoApplication
import com.unex.asee.ga02.beergo.R
import com.unex.asee.ga02.beergo.model.Beer
import com.unex.asee.ga02.beergo.model.User
import com.unex.asee.ga02.beergo.repository.BeerRepository
import kotlinx.coroutines.launch

class InsertBeerViewModel(
    private var beerRepository: BeerRepository,
    private val application: BeerGoApplication
): ViewModel() {

    var user : User? = null
    val beer : Beer? = null
    var selectedImageUri: Uri? = null



    /**
     *  Añade una cerveza a la base de datos local.
     *
     *  @param beer la Cerveza que se insertará.
     *  @return Método addBeer de beerRepository.
     */
    suspend fun addBeer (beer: Beer){
        return beerRepository.addBeer(beer)
    }

    private val _toast = MutableLiveData<String?>()

    val toast: LiveData<String?>
        get() = _toast

    /**
     * Crea una instancia de la clase [Beer] con los datos proporcionados.
     *
     * @param title Nombre de la cerveza.
     * @param description Descripción de la cerveza.
     * @param year Año de la cerveza.
     * @param abv Porcentaje de alcohol.
     * @param image URL de la imagen de la cerveza.
     * @param insertedBy ID del usuario que insertó la cerveza.
     * @return Una instancia de la clase [Beer].
     */
    fun createBeer(
        title: String,
        description: String,
        year: String,
        abv: Double,
        imageUri: Uri?
    ): Beer {
        val defaultImageUri = "android.resource://${application.packageName}/${R.drawable.default_image}"
        val image = imageUri?.toString() ?: defaultImageUri
        return Beer(
            beerId = 0,
            title = title,
            description = description,
            year = year,
            abv = abv,
            image = image,
            insertedBy = user?.userId
        )
    }

    fun onToastShown(){
        _toast.value = null
    }

    /**
     * Verifica si los campos necesarios para la inserción son válidos.
     *
     * @param title Nombre de la cerveza.
     * @param description Descripción de la cerveza.
     * @param year Año de la cerveza.
     * @param abvString Porcentaje de alcohol en formato de cadena.
     * @return `true` si los campos son válidos, `false` de lo contrario.
     */
    fun areFieldsValid(
        title: String,
        description: String,
        year: String,
        abvString: String
    ): Boolean {
        return title.isNotEmpty() && description.isNotEmpty() && year.isNotEmpty() && abvString.isNotEmpty()
    }


    /**
     * Se encarga de llevar a cabo el proceso de la inserción
     *
     */
    fun procesoInsertar(title: String,
                        description: String,
                        year: String,
                        abvString: String){

        if (areFieldsValid(title, description, year, abvString)) {
            val abv = abvString.toDoubleOrNull()

            if (abv != null) {
                val beer: Beer
                if (selectedImageUri != null) {
                    beer = createBeer(title, description, year, abv, selectedImageUri)
                } else {
                    beer = createBeer(title, description, year, abv, null) // Pasa null si no hay imagen seleccionada
                }
                viewModelScope.launch() {
                    addBeer(beer)
                    _toast.value = "Cerveza insertada"

                }
            } else {
                // Manejar el caso donde el campo de porcentaje de alcohol no es un número válido
                handleInvalidAbv()
            }
        } else {
            // Manejar el caso donde los campos no están completos
            handleIncompleteFields()
        }
    }

    /**
     * Maneja el caso donde el campo de porcentaje de alcohol no es un número válido.
     */
    private fun handleInvalidAbv() {
        // Manejar el caso donde el campo de porcentaje de alcohol no es un número válido
        _toast.value = "Porcentaje de alcohol no válido. Introduce un número válido."
    }

    /**
     * Maneja el caso donde los campos no están completos.
     */
    private fun handleIncompleteFields() {
        // Manejar el caso donde los campos no están completos
        _toast.value = "Completa todos los campos antes de insertar la cerveza."
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
                return InsertBeerViewModel(
                    (application as BeerGoApplication).appContainer.beerRepository,
                    (application)
                ) as T
            }
        }
    }
}