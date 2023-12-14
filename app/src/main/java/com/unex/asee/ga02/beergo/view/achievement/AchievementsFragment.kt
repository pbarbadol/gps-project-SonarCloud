package com.unex.asee.ga02.beergo.view.achievement

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.unex.asee.ga02.beergo.databinding.FragmentAchievementsBinding
import com.unex.asee.ga02.beergo.model.Achievement
import com.unex.asee.ga02.beergo.model.User
import com.unex.asee.ga02.beergo.view.viewmodel.AchievementsViewModel
import com.unex.asee.ga02.beergo.view.viewmodel.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Fragmento que muestra la lista de logros.
 */
class AchievementsFragment : Fragment() {

    private val viewModel: AchievementsViewModel by viewModels { AchievementsViewModel.Factory }
    private val homeViewModel: HomeViewModel by activityViewModels()

    // View Binding
    private var _binding: FragmentAchievementsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: AchievementsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        homeViewModel.user.observe(viewLifecycleOwner) { user ->
            viewModel.user = user
        }


                // Ocultar el ProgressBar
                //binding.loadingProgressBar.visibility = View.GONE

                // Crear y configurar el adaptador
                adapter = AchievementsAdapter(achievements = emptyList(),
                    userAchievements = emptyList(),
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


        viewModel.achievements.observe(viewLifecycleOwner) { achievements ->
            adapter.updateData(achievements)
        }
        viewModel.achievementsUser.observe(viewLifecycleOwner) { achievementsUser ->
            adapter.updateDataUser(achievementsUser.achievements)
        }
        viewModel.spinner.observe(viewLifecycleOwner){visible->
            binding.Achievementspinner.visibility = if (visible) View.VISIBLE else View.GONE
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Evitar fugas de memoria
    }

    /**
     * Realiza una consulta de logros en la base de datos.
     */


}