package com.unex.asee.ga02.beergo.view.achievement

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.unex.asee.ga02.beergo.database.BeerGoDatabase
import com.unex.asee.ga02.beergo.databinding.FragmentAchievementsBinding
import com.unex.asee.ga02.beergo.model.Achievement
import com.unex.asee.ga02.beergo.model.User
import com.unex.asee.ga02.beergo.repository.AchievementRepository
import com.unex.asee.ga02.beergo.view.viewmodel.UserViewModel
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
    private lateinit var achievementRepository: AchievementRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Obtener el ViewModel
        userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        // Obtener el usuario actual
        currentUser = userViewModel.getUser()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflar el diseño del fragmento
        _binding = FragmentAchievementsBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Obtener la instancia de la base de datos
        db = BeerGoDatabase.getInstance(this.requireContext())!!
        // Obtener la instancia del repositorio
        achievementRepository = AchievementRepository.getInstance(db.userDao(), db.achievementDao())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch(Dispatchers.IO) {
            // Muestra el ProgressBar
            binding.loadingProgressBar.visibility = View.VISIBLE
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
        achievements = achievementRepository.getAllAchievements()

        // Obtener la lista de logros del usuario
        val userWithAchievements = achievementRepository.getUserAchievements(currentUser.userId)
        userAchievements = userWithAchievements.achievements
    }

}