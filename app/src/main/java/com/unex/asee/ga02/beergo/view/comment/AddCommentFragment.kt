package com.unex.asee.ga02.beergo.view.comment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.unex.asee.ga02.beergo.BeerGoApplication
import com.unex.asee.ga02.beergo.R
import com.unex.asee.ga02.beergo.database.BeerGoDatabase
import com.unex.asee.ga02.beergo.databinding.FragmentAddcommentBinding
import com.unex.asee.ga02.beergo.model.Comment
//import com.unex.asee.ga02.beergo.utils.ChallengeAchievementFunction.ChallengeAchievementObserver
import com.unex.asee.ga02.beergo.view.viewmodel.AddCommentViewModel
import com.unex.asee.ga02.beergo.view.viewmodel.CheckViewModel
import com.unex.asee.ga02.beergo.view.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

class AddCommentFragment : Fragment() {
    private val viewModel: AddCommentViewModel by viewModels { AddCommentViewModel.Factory }
    private val homeViewModel: HomeViewModel by activityViewModels()
    private val viewModelCheckAchievement : CheckViewModel by viewModels{ CheckViewModel.Factory }

    private var _binding: FragmentAddcommentBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddcommentBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Observamos el usuario de HomeViewModel y se lo asignamos a nuestro viewModel
        homeViewModel.user.observe(viewLifecycleOwner) { user ->
            viewModel.user = user
            viewModelCheckAchievement.user = user
        }
        homeViewModel.beer.observe(viewLifecycleOwner) { beer ->
            viewModel.beer = beer
        }
        binding.btAccept.setOnClickListener {
            viewModel.writeComment(view.findViewById<EditText>(R.id.editTextText).text.toString()) //toTrim() ¿?
            viewModelCheckAchievement.checkAchievementsComment()
            findNavController().popBackStack()

            }

        binding.btCancel.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}