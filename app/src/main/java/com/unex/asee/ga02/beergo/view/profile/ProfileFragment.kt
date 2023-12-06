package com.unex.asee.ga02.beergo.view.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.unex.asee.ga02.beergo.BeerGoApplication
import com.unex.asee.ga02.beergo.database.BeerGoDatabase
import com.unex.asee.ga02.beergo.databinding.FragmentProfileBinding
import com.unex.asee.ga02.beergo.model.Achievement
import com.unex.asee.ga02.beergo.model.User
import com.unex.asee.ga02.beergo.repository.UserRepository
import com.unex.asee.ga02.beergo.view.home.LoginActivity
import com.unex.asee.ga02.beergo.view.viewmodel.ProfileViewModel
import com.unex.asee.ga02.beergo.view.viewmodel.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var nivel: Int = 0
    private var exp: Int = 0
    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileViewModel by viewModels { ProfileViewModel.Factory }
    private var achievements: List<Achievement> = emptyList()
    private var userAchievements: List<Achievement> = emptyList()
    private lateinit var currentUser: User
    private lateinit var db: BeerGoDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentUser = viewModel.getCurrentUser()!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Obtenemos los datos del contenedor de dependencias
        val appContainer = (this.activity?.application as BeerGoApplication).appContainer
        db = appContainer.db!!

        binding.idUser.text = currentUser.name
        lifecycleScope.launch(Dispatchers.IO) {


            launch(Dispatchers.Main) {

                // Ahora puedes iniciar la verificación de logros
            }
            consultaLogros()
            obtenerNivel()

            binding.progressBar.progress = exp.toInt()
            binding.levelTextView.text = "Nivel $nivel"
            binding.expTextView.text = "${exp}%"
        }


        // Accede a las vistas a través del binding
        binding.idUser.text = viewModel.getCurrentUser()?.name
        lifecycleScope.launch(Dispatchers.Main) {
            setUpStadistics()
        }
        binding.eliminarUsuario.setOnClickListener {
            val user = viewModel.getCurrentUser()

            if (user != null) {
                lifecycleScope.launch(Dispatchers.IO) {
                    viewModel.deleteUser(user)
                    activity?.finish()
                    val intent = Intent(context, LoginActivity::class.java)
                    startActivity(intent)
                }
            }
        }

        binding.cerrarSesion.setOnClickListener {
            val emptyUser = User(userId = 0, name = "", password = "")
            viewModel.setUser(emptyUser)
            viewModel.setUser(User(0, "", ""))
            activity?.finish()
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private suspend fun setUpStadistics() {
        binding.cervezasAnadidas.text = "Cervezas Añadidas: ${
            viewModel.countBeersInsertedByUser(viewModel.getCurrentUser()!!.userId)
        }"
        binding.cervezasFavoritas.text = "Cervezas Favoritas: ${
            viewModel.countFavouritesByUser(viewModel.getCurrentUser()!!.userId)
        }"
        binding.comentariosAnadidos.text = "Comentarios Añadidos: ${
            viewModel.countCommentsByUser(viewModel.getCurrentUser()!!.userId)
        }"
        binding.logrosConseguidos.text = "Logros Conseguidos: ${
            viewModel.countUserAchievements(viewModel.getCurrentUser()!!.userId)
        }"
    }

    private suspend fun consultaLogros() {
        // Obtener la lista de logros
        achievements = viewModel.getAllAchievements()

        // Obtener la lista de logros del usuario
        val userWithAchievements = viewModel.getUserAchievements(currentUser.userId)
        userAchievements = userWithAchievements.achievements
    }

    private fun obtenerNivel() {

        for (achievement in userAchievements) {
            exp += achievement.expPoint
        }
        while (exp >= 100) {
            nivel++
            exp -= 100
        }

    }
}
