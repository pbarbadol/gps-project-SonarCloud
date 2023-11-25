package com.unex.asee.ga02.beergo.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.unex.asee.ga02.beergo.data.beerAchievements
import com.unex.asee.ga02.beergo.database.BeerGoDatabase
import com.unex.asee.ga02.beergo.databinding.FragmentAchievementsBinding
import com.unex.asee.ga02.beergo.model.Achievement
import com.unex.asee.ga02.beergo.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Fragmento que muestra la lista de logros.
 */
class AchievementsFragment : Fragment() {

    private lateinit var db: BeerGoDatabase

    // View Binding
    private var _binding: FragmentAchievementsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: AchievementsAdapter
    private var achievements: List<Achievement> = emptyList()
    private var userAchievements: List<Achievement> = emptyList()
    private lateinit var userViewModel: UserViewModel
    private lateinit var currentUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Obtener el ViewModel
        userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        // Obtener el usuario actual
        currentUser = userViewModel.getUser()
        // Obtener instancia de la db
        db = BeerGoDatabase.getInstance(this.requireContext())!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflar el diseño del fragmento
        _binding = FragmentAchievementsBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch(Dispatchers.IO) {
            // Muestra el ProgressBar
            binding.loadingProgressBar.visibility = View.VISIBLE
            almacenarBD()
            consultaLogros()

            withContext(Dispatchers.Main) {
                // Ocultar el ProgressBar
                binding.loadingProgressBar.visibility = View.GONE

                // Crear y configurar el adaptador
                adapter = AchievementsAdapter(achievements = achievements,
                    userAchievements = userAchievements,
                    onClick = { achievement ->
                        // Manejar clic en el logro
                        Toast.makeText(
                            context, "Click on: ${achievement.description}", Toast.LENGTH_SHORT
                        ).show()
                    },
                    onLongClick = { achievement ->
                        // Manejar clic largo en el logro
                        Toast.makeText(
                            context, "Long click on: ${achievement.title}", Toast.LENGTH_SHORT
                        ).show()
                    })

                // Configurar el RecyclerView con el adaptador
                with(binding) {
                    rvAchievementList.layoutManager =
                        androidx.recyclerview.widget.LinearLayoutManager(context)
                    rvAchievementList.adapter = adapter
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Evitar fugas de memoria
    }

    /**
     * Realiza una consulta de logros en la base de datos.
     */
    private suspend fun consultaLogros() {
        // Obtener la lista de logros
        achievements = db.achievementDao().getAll()

        // Obtener la lista de logros del usuario
        val userWithAchievements = db.achievementDao().getUserWithAchievements(currentUser.userId)
        userAchievements = userWithAchievements.achievements
    }


    /**
     * Almacena logros en la base de datos.
     */
    private suspend fun almacenarBD() {
        // Insertar un logro
        for (achievement in beerAchievements) {
            db.achievementDao().insert(achievement)
        }
        /*
        // Insertar un logro y relacionarlo con un usuario
        db.achievementDao().insertAndRelate(beerAchievements[2], currentUser.userId)
        db.achievementDao().insertAndRelate(beerAchievements[5], currentUser.userId)*/
    }
}