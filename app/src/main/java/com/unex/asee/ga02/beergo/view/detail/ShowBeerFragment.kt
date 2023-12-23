package com.unex.asee.ga02.beergo.view.detail

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.unex.asee.ga02.beergo.BeerGoApplication
import com.unex.asee.ga02.beergo.database.BeerGoDatabase
import com.unex.asee.ga02.beergo.databinding.FragmentShowBeerBinding
import com.unex.asee.ga02.beergo.view.viewmodel.CheckViewModel
import com.unex.asee.ga02.beergo.view.viewmodel.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
//import com.unex.asee.ga02.beergo.utils.ChallengeAchievementFunction.ChallengeAchievementObserver
import com.unex.asee.ga02.beergo.view.viewmodel.ShowBeerViewModel

class ShowBeerFragment : Fragment() {
    private val viewModel: ShowBeerViewModel by viewModels { ShowBeerViewModel.Factory }
    private val homeViewModel: HomeViewModel by activityViewModels()
    private val viewModelCheckAchievement : CheckViewModel by viewModels{ CheckViewModel.Factory }
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.user.observe(viewLifecycleOwner) { user ->
            Log.d("Observation", "User observed: $user")
            viewModel.user = user
            viewModelCheckAchievement.user = user
        }
        viewModelCheckAchievement.toast.observe(viewLifecycleOwner){text->
            text?.let {
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
                viewModelCheckAchievement.onToastShown()
            }
        }

        homeViewModel.beer.observe(viewLifecycleOwner) { beer ->
            viewModel.beer = beer
            showBinding()
            Log.d("Observation", "Beer observed: ${viewModel.beer}")
        }
        Log.d("Observation", "Beer observed2: ${viewModel.beer}")


    }

    fun showBinding() {
        Log.d("Observation", "Beer observed3: ${viewModel.beer}")
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

        viewModel.favBeers.observe(viewLifecycleOwner) { favBeers ->
            Log.d("ObservationFavorite", "favBeers: $favBeers")
        }
        viewModel.isFavourite.observe(viewLifecycleOwner) { isFavourite ->
            binding.favSwitch.isChecked = isFavourite
        }

            binding.favSwitch.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    viewModel.addFav()
                    viewModelCheckAchievement.checkAchievementsFav()
                } else {
                    viewModel.deleteFav()
                }
            }

    }

}