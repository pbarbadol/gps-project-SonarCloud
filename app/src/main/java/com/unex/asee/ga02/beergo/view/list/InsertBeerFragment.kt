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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.user.observe(viewLifecycleOwner) { user ->
            viewModel.user = user
        }

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
            viewModel.procesoInsertar(binding.editTextBeerName.text.toString(),
                                      binding.editTextBeerDescription.text.toString(),
                                      binding.editTextYear.text.toString(),
                                      binding.editTextAlcoholPercentage.text.toString())

        }
    }


    /*
     * Lanza un Intent para abrir la galería de imágenes para poder seleccionar una imagen
     */
    private fun openGalleryForImage() {
        Intent(Intent.ACTION_PICK).type = "image/*"
        startActivityForResult(Intent(Intent.ACTION_PICK), GALLERY_REQUEST_CODE)
    }

    /**
     * Método llamado cuando la vista y cualquier fragmento asociado con la vista son eliminados.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Evitar fugas de memoria
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
                viewModel.selectedImageUri = uri // Asignar la URI seleccionada a la variable de clase
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