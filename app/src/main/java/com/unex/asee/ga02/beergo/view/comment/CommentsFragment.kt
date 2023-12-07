package com.unex.asee.ga02.beergo.view.comment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.unex.asee.ga02.beergo.databinding.FragmentCommentsBinding
import com.unex.asee.ga02.beergo.model.Comment
import com.unex.asee.ga02.beergo.view.viewmodel.CommentsViewModel
import kotlinx.coroutines.launch


class CommentsFragment: Fragment() {

    private val viewModel: CommentsViewModel by viewModels { CommentsViewModel.Factory }
    private lateinit var listener: OnShowClickListener
    interface OnShowClickListener {
        fun onShowClick(comment: Comment)
    }
    private var _binding: FragmentCommentsBinding? = null
    private val binding get() = _binding!!
    private lateinit  var adapter: CommentsAdapter
    private var beerComments = emptyList<Comment>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: android.content.Context) {
        super.onAttach(context)

        if (context is CommentsFragment.OnShowClickListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnShowClickListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCommentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(View: View, savedInstanceState: Bundle?) {
        super.onViewCreated(View, savedInstanceState)
        //Obtenemos los datos del contenedor de dependencias de la aplicacion

        val cerveza = viewModel.getSelectedBeer()
        Log.d("ShowBeerFragment", "El id de la cerveza en Comments es ${cerveza?.beerId}")

        val user = viewModel.getCurrentUser()
        Log.d("ShowBeerFragment", "El id del usuario en Comments es ${user?.userId}")
        setUpRecyclerView()
        loadComments()

        binding.addCommentButton.setOnClickListener{
            val action = CommentsFragmentDirections.actionCommentsFragmentToAddCommentFragment()
            findNavController().navigate(action)
        }
    }

    fun setUpUI() {
    }

    private fun setUpRecyclerView() {
        adapter = CommentsAdapter(comments = beerComments, onClick = {

//            listener.onShowClick(it)
        },
            onLongClick = {
                deleteComment(it)
                loadComments()
            }
        )
        with (binding) {
            val numberOfColumns = 1
            rvCommentsList.layoutManager = GridLayoutManager(context, numberOfColumns)
            rvCommentsList.adapter = adapter
        }
        Log.d("DiscoverFragment", "setUpRecyclerView")
    }

    private fun deleteComment(comment: Comment) {
        val userCommentId = comment.userId
        val user= viewModel.getCurrentUser()
        val userId = user?.userId
        if(userId == userCommentId){
            lifecycleScope.launch {
                viewModel.deleteComment(comment)
                Toast.makeText(
                    requireContext(),
                    "Se ha borrado el comentario con ID: ${comment.commentId}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(
                requireContext(),
                "No eres el creador de este comentario",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    private fun loadComments() { //TODO: Aqui deberá usarse viewModel ¿?
        val beer = viewModel.getSelectedBeer()
        beer?.let {
            val idCerveza = it.beerId
            lifecycleScope.launch {
                binding.spinner.visibility = View.VISIBLE
                beerComments = viewModel.loadComments(idCerveza)
                adapter.updateData(beerComments)
                binding.spinner.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // avoid memory leaks
    }

}