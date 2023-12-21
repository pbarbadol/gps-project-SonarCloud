package com.unex.asee.ga02.beergo.view.comment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.unex.asee.ga02.beergo.databinding.FragmentCommentsBinding
import com.unex.asee.ga02.beergo.model.Comment
import com.unex.asee.ga02.beergo.view.viewmodel.CheckViewModel
import com.unex.asee.ga02.beergo.view.viewmodel.CommentsViewModel
import com.unex.asee.ga02.beergo.view.viewmodel.HomeViewModel
import kotlinx.coroutines.launch


class CommentsFragment : Fragment() {

    private val viewModel: CommentsViewModel by viewModels { CommentsViewModel.Factory }
    private val homeViewModel: HomeViewModel by activityViewModels()
    private lateinit var listener: OnShowClickListener
    private var _binding: FragmentCommentsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: CommentsAdapter

    interface OnShowClickListener {
        fun onShowClick(comment: Comment)
    }

    override fun onAttach(context: android.content.Context) {
        super.onAttach(context)

        if (context is CommentsFragment.OnShowClickListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnShowClickListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCommentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(View: View, savedInstanceState: Bundle?) {
        super.onViewCreated(View, savedInstanceState)
        homeViewModel.user.observe(viewLifecycleOwner) { user ->
            Log.d("Observation", "User observed: $user")
            viewModel.user = user
        }
        homeViewModel.beer.observe(viewLifecycleOwner) { beer ->
            viewModel.beer = beer
        }

        Log.d("Observation", "bEER observed2: ${viewModel.beer}")
        viewModel.toast.observe(viewLifecycleOwner) { text ->
            text?.let {
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
                viewModel.onToastShown()
            }
        }
        //Inicializamos el adapter
        setUpRecyclerView()

        //En caso de cambio, lo actualizamos
        viewModel.beerComment.observe(viewLifecycleOwner) { comments ->
                adapter.updateData(comments)
        }

        binding.addCommentButton.setOnClickListener {
            findNavController().navigate(CommentsFragmentDirections.actionCommentsFragmentToAddCommentFragment())
        }

        homeViewModel.liveDataAchievements.observe(viewLifecycleOwner) {
            Toast.makeText(context, "Logro conseguido", Toast.LENGTH_SHORT).show()
        }

    }

    private fun setUpRecyclerView() {
        adapter = CommentsAdapter(comments = emptyList(), onClick = {}, onLongClick = {
            viewModel.deleteComment(it)
        })
        with(binding) {
            rvCommentsList.layoutManager = GridLayoutManager(context, 1)
            rvCommentsList.adapter = adapter
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // avoid memory leaks
    }
}