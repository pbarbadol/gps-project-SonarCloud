package com.unex.asee.ga02.beergo.view.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.unex.asee.ga02.beergo.database.BeerGoDatabase
import com.unex.asee.ga02.beergo.databinding.FragmentProfileBinding
import com.unex.asee.ga02.beergo.model.Achievement
import com.unex.asee.ga02.beergo.model.User
import com.unex.asee.ga02.beergo.view.home.LoginActivity
import com.unex.asee.ga02.beergo.view.viewmodel.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var nivel: Int = 0
    private var exp: Int = 0
    private lateinit var binding: FragmentProfileBinding
    private lateinit var userViewModel: UserViewModel
    private var achievements: List<Achievement> = emptyList()
    private var userAchievements: List<Achievement> = emptyList()
    private lateinit var currentUser: User
    private lateinit var db: BeerGoDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        db = BeerGoDatabase.getInstance(requireContext())!!
        currentUser = userViewModel.getUser()
        db = BeerGoDatabase.getInstance(this.requireContext())!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.idUser.text = currentUser.name
        // Agrega estas variables a tu clase
        lifecycleScope.launch(Dispatchers.IO) {


            // Después de obtener la información de logros, puedes actualizar la interfaz de usuario
            // Esto debe hacerse en el hilo principal, por lo que utilizamos launch(Dispatchers.Main)
            launch(Dispatchers.Main) {
                // Agrega el código aquí para actualizar la interfaz de usuario según los logros obtenidos
                // Puedes mostrar la información de logros en tu diseño (por ejemplo, el número de logros, etc.).

                // Ahora puedes iniciar la verificación de logros
            }
            consultaLogros()
            obtenerNivel()

            binding.progressBar.progress = exp.toInt()
            binding.levelTextView.text = "Nivel $nivel"
            binding.expTextView.text = "${exp}%"
        }


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
            val emptyUser = User(userId = 0, name = "", password = "")
            userViewModel.setUser(emptyUser)
            userViewModel.setUser(User(0, "", ""))
            activity?.finish()
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private suspend fun setUpStadistics() {
        binding.cervezasAnadidas.text = "Cervezas Añadidas: ${
            db.userDao().countBeersInsertedByUser(userViewModel.getUser().userId)
        }"
        binding.cervezasFavoritas.text = "Cervezas Favoritas: ${
            db.userDao().countUserFavouriteBeers(userViewModel.getUser().userId)
        }"
        binding.comentariosAnadidos.text = "Comentarios Añadidos: ${
            db.userDao().countCommentsByUser(userViewModel.getUser().userId)
        }"
        binding.logrosConseguidos.text = "Logros Conseguidos: ${
            db.userDao().countUserAchievements(userViewModel.getUser().userId)
        }"
        //binding.iniciosSesion.text = "Inicios de Sesión: ${0}"
        //binding.fechaCreacion.text = "Fecha de Creación: ${0}"

    }

    private suspend fun consultaLogros() {
        // Obtener la lista de logros
        achievements = db.achievementDao().getAll()

        // Obtener la lista de logros del usuario
        val userWithAchievements = db.achievementDao().getUserWithAchievements(currentUser.userId)
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
