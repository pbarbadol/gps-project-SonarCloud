package com.unex.asee.ga02.beergo.view.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.unex.asee.ga02.beergo.databinding.FragmentProfileBinding
import com.unex.asee.ga02.beergo.model.User
import com.unex.asee.ga02.beergo.view.home.LoginActivity
import com.unex.asee.ga02.beergo.view.viewmodel.HomeViewModel
import com.unex.asee.ga02.beergo.view.viewmodel.ProfileViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileViewModel by viewModels { ProfileViewModel.Factory }
    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Observamos el usuario de HomeViewModel y se lo asignamos a nuestro viewModel
        homeViewModel.user.observe(viewLifecycleOwner) { user ->
            viewModel.user = user
        }
        //El viewModel se encargará solo de calcular el nivel y la experiencia
        binding.idUser.text = viewModel.user?.name
        binding.progressBar.progress = viewModel.exp
        binding.levelTextView.text = "Nivel ${viewModel.nivel}"
        binding.expTextView.text = "${viewModel.exp}%"
        binding.idUser.text = viewModel.user?.name
        setUpStadistics()
        //Eliminar usuario
        binding.eliminarUsuario.setOnClickListener {
            viewModel.deleteUser { //lambda
                activity?.finish()
                val intent = Intent(context, LoginActivity::class.java)
                startActivity(intent)
            }
        }
        //Cerrar sesión
        binding.cerrarSesion.setOnClickListener {
            viewModel.cerrarSesion {
                activity?.finish()
                val intent = Intent(context, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun setUpStadistics() {
        binding.cervezasAnadidas.text = "Cervezas Añadidas: ${
            viewModel.countBeersInsertedByUser()
        }"
        binding.cervezasFavoritas.text = "Cervezas Favoritas: ${
            viewModel.countFavouritesByUser()
        }"
        binding.comentariosAnadidos.text = "Comentarios Añadidos: ${
            viewModel.countCommentsByUser()
        }"
        binding.logrosConseguidos.text = "Logros Conseguidos: ${
            viewModel.countUserAchievements()
        }"
    }
}
