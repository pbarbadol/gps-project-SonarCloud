package com.unex.asee.ga02.beergo.view.home

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    private  var nivel : Int = 0
    private  var exp : Int = 0
    private lateinit var binding: FragmentProfileBinding
    private lateinit var userViewModel: UserViewModel
    private var achievements: List<Achievement> = emptyList()
    private var userAchievements: List<Achievement> = emptyList()
    private lateinit var currentUser: User
    private lateinit var db: BeerGoDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        currentUser = userViewModel.getUser()
        db = BeerGoDatabase.getInstance(this.requireContext())!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentProfileBinding.inflate(inflater, container, false)

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

            binding.progressBar.progress= exp.toInt()
            binding.levelTextView.text = "Nivel $nivel"
            binding.expTextView.text = "${exp}%"
        }

        binding.eliminarUsuario.setOnClickListener {
            val user = userViewModel.getUser()

            if (user != null) {
                lifecycleScope.launch(Dispatchers.IO) {
                    db?.userDao()?.delete(user)
                    activity?.finish()
                    val intent = Intent(context, LoginActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        binding.cerrarSesion.setOnClickListener {
            val emptyUser = User(userId = 0, name = "", password = "")
            userViewModel.setUser(emptyUser)
            activity?.finish()
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
        }
            // Aquí es donde realizarías la operación de eliminación en la base de datos.
                // Esto dependerá de cómo estés manejando las operaciones de la base de datos en tu aplicación.

        }

    private suspend fun consultaLogros() {
        // Obtener la lista de logros
        achievements = db.achievementDao().getAll()

        // Obtener la lista de logros del usuario
        val userWithAchievements = db.achievementDao().getUserWithAchievements(currentUser.userId)
        userAchievements = userWithAchievements.achievements
    }

    private  fun obtenerNivel() {

        for (achievement in userAchievements){
            exp += achievement.expPoint
        }
        while (exp >= 100){
            nivel++
            exp -= 100
        }

    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}