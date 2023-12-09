package com.unex.asee.ga02.beergo.view.list

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.unex.asee.ga02.beergo.BeerGoApplication
import com.unex.asee.ga02.beergo.R
import com.unex.asee.ga02.beergo.databinding.FragmentInsertBeerBinding
import com.unex.asee.ga02.beergo.model.Beer
import com.unex.asee.ga02.beergo.view.viewmodel.HomeViewModel
//import com.unex.asee.ga02.beergo.utils.ChallengeAchievementFunction.ChallengeAchievementObserver
import com.unex.asee.ga02.beergo.view.viewmodel.InsertBeerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Fragmento para la inserción de nuevas cervezas.
 */
class InsertBeerFragment : Fragment() {

    // View Binding
    private var _binding: FragmentInsertBeerBinding? = null
    private val binding get() = _binding!!
    private var selectedImageUri: Uri? = null
    private val viewModel: InsertBeerViewModel by viewModels { InsertBeerViewModel.Factory }
    private val homeViewModel: HomeViewModel by activityViewModels()

    /**
     * Método llamado cuando se crea la instancia del fragmento.
     *
     * @param savedInstanceState Bundle que contiene el estado previamente guardado del fragmento.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    /**
     * Método llamado para crear y devolver la vista asociada con el fragmento.
     *
     * @param inflater El objeto LayoutInflater que puede ser utilizado para inflar cualquier vista en el fragmento.
     * @param container Si no es nulo, este es el grupo de vistas al que se adjuntará el fragmento.
     * @param savedInstanceState Si no es nulo, este fragmento está siendo reconstruido a partir de un estado guardado.
     * @return La vista raíz del fragmento.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInsertBeerBinding.inflate(inflater, container, false)

        setupInsertButton()
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val appContainer = (this.activity?.application as BeerGoApplication).appContainer
    }

    /**
     * Configura el botón de inserción para manejar la lógica de inserción de cervezas.
     */
    private fun setupInsertButton() {

        /*
         * Define la acción del botón de seleccionar imagen
         */
        binding.buttonSelectImage.setOnClickListener {
            openGalleryForImage()
        }

        binding.buttonInsertBeer.setOnClickListener {
            val title = binding.editTextBeerName.text.toString()
            val description = binding.editTextBeerDescription.text.toString()
            val year = binding.editTextYear.text.toString()
            val abvString = binding.editTextAlcoholPercentage.text.toString()

            if (areFieldsValid(title, description, year, abvString)) {
                val abv = abvString.toDoubleOrNull()

                if (abv != null) {
                    val beer: Beer
                    if (selectedImageUri != null) {
                        beer = createBeer(title, description, year, abv, selectedImageUri)
                    } else {
                        beer = createBeer(title, description, year, abv, null) // Pasa null si no hay imagen seleccionada
                    }
                    lifecycleScope.launch(Dispatchers.IO) {
                        insertarCerveza(beer)
                        showNotification("Cerveza insertada")

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
    }


    /*
     * Lanza un Intent para abrir la galería de imágenes para poder seleccionar una imagen
     */
    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
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
    private fun areFieldsValid(
        title: String,
        description: String,
        year: String,
        abvString: String
    ): Boolean {
        return title.isNotEmpty() && description.isNotEmpty() && year.isNotEmpty() && abvString.isNotEmpty()
    }

    /**
     * Maneja el caso donde el campo de porcentaje de alcohol no es un número válido.
     */
    private fun handleInvalidAbv() {
        // Manejar el caso donde el campo de porcentaje de alcohol no es un número válido
        showNotification("Porcentaje de alcohol no válido. Introduce un número válido.")
    }

    /**
     * Maneja el caso donde los campos no están completos.
     */
    private fun handleIncompleteFields() {
        // Manejar el caso donde los campos no están completos
        showNotification("Completa todos los campos antes de insertar la cerveza.")
    }

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
    private fun createBeer(
        title: String,
        description: String,
        year: String,
        abv: Double,
        imageUri: Uri?
    ): Beer {
        val defaultImageUri = "android.resource://${requireActivity().packageName}/${R.drawable.default_image}"
        val image = imageUri?.toString() ?: defaultImageUri
        return Beer(
            beerId = 0,
            title = title,
            description = description,
            year = year,
            abv = abv,
            image = image,
            insertedBy = viewModel.user?.userId
        )
    }

    /**
     * Inserta una nueva cerveza en la base de datos de BeerGo de manera asíncrona.
     *
     * @param beer La cerveza que se va a insertar en la base de datos.
     */
    private suspend fun insertarCerveza(beer: Beer) {
        viewModel.addBeer(beer)
        // Notificar a los observadores de desafíos
        //db.notifyDatabaseObservers("UserBeerCrossRef") TODO: observer
    }

    /**
     * Método llamado cuando la vista y cualquier fragmento asociado con la vista son eliminados.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Evitar fugas de memoria
    }

    private fun showNotification(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    /*
     * Se encarga de manejar la respuesta del Intent lanzado a la galería para poder obtener la URI de
     * la imagen seleccionada
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                binding.imageViewSelected.setImageURI(uri)
                selectedImageUri = uri // Asignar la URI seleccionada a la variable de clase
            }
        }
    }

    /*
     * Proporciona un identificador para manejar la solicitud a la galería
     */
    companion object {
        private const val GALLERY_REQUEST_CODE = 100
    }
}