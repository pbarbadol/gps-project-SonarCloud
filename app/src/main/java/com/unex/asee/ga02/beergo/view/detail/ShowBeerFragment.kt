package com.unex.asee.ga02.beergo.view.detail

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.unex.asee.ga02.beergo.BeerGoApplication
import com.unex.asee.ga02.beergo.database.BeerGoDatabase
import com.unex.asee.ga02.beergo.databinding.FragmentShowBeerBinding
import com.unex.asee.ga02.beergo.view.viewmodel.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
//import com.unex.asee.ga02.beergo.utils.ChallengeAchievementFunction.ChallengeAchievementObserver
import com.unex.asee.ga02.beergo.view.viewmodel.ShowBeerViewModel

class ShowBeerFragment : Fragment() {
    private val viewModel: ShowBeerViewModel by viewModels { ShowBeerViewModel.Factory }
    private val homeViewModel: HomeViewModel by activityViewModels()
    private var _binding: FragmentShowBeerBinding? = null

    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShowBeerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(View: View, savedInstanceState: Bundle?) {
        super.onViewCreated(View, savedInstanceState)

        homeViewModel.user.observe(viewLifecycleOwner) { user ->
            Log.d("Observation", "User observed: $user")
            viewModel.user = user
        }

        binding.id.text = viewModel.beer?.beerId.toString()
        binding.title.text = viewModel.beer?.title
        binding.anio.text = viewModel.beer?.year
        binding.description.text = viewModel.beer?.description
        binding.abv.text = viewModel.beer?.abv.toString() + "%"
        Glide.with(this).load(viewModel.beer?.image).into(binding.beerImage)
        //Navegación a AddCommentFragment
        binding.imageView7.setOnClickListener {
            val navController = findNavController()
            navController.navigate(ShowBeerFragmentDirections.actionShowBeerFragmentToCommentsFragment())
        }
        viewModel.isFavourite.observe(viewLifecycleOwner) {isFavourite ->
            binding.favSwitch.isChecked = isFavourite

            binding.favSwitch.setOnCheckedChangeListener { _, isChecked ->
                if (isFavourite) {
                    viewModel.addFav()
                } else {
                    viewModel.deleteFav()
                }
            }
        }
    }

}