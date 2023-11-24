package com.unex.asee.ga02.beergo.view.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.unex.asee.ga02.beergo.database.BeerGoDatabase
import com.unex.asee.ga02.beergo.databinding.FragmentProfileBinding
import com.unex.asee.ga02.beergo.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var db: BeerGoDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        db = BeerGoDatabase.getInstance(requireContext())!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Accede a las vistas a través del binding
        binding.idUser.text = userViewModel.getUser()?.name
        lifecycleScope.launch(Dispatchers.Main) {
            setUpStadistics()
        }
        binding.eliminarUsuario.setOnClickListener {
            val user = userViewModel.getUser()

            if (user != null) {
                lifecycleScope.launch(Dispatchers.IO) {
                    db.userDao().delete(user)
                    activity?.finish()
                    val intent = Intent(context, LoginActivity::class.java)
                    startActivity(intent)
                }
            }
        }

        binding.cerrarSesion.setOnClickListener {
            userViewModel.setUser(User(0, "", ""))
            activity?.finish()
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private suspend fun setUpStadistics() {
        binding.cervezasAnadidas.text = "Cervezas Añadidas: ${db.userDao().countBeersInsertedByUser(userViewModel.getUser().userId)}"
        binding.cervezasFavoritas.text = "Cervezas Favoritas: ${db.userDao().countUserFavouriteBeers(userViewModel.getUser().userId)}"
        binding.comentariosAnadidos.text = "Comentarios Añadidos: ${db.userDao().countCommentsByUser(userViewModel.getUser().userId)}"
        binding.logrosConseguidos.text = "Logros Conseguidos: ${db.userDao().countUserAchievements(userViewModel.getUser().userId)}"
        //binding.iniciosSesion.text = "Inicios de Sesión: ${0}"
        //binding.fechaCreacion.text = "Fecha de Creación: ${0}"
    }
}
