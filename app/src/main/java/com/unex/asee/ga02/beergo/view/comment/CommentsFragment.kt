package com.unex.asee.ga02.beergo.view.comment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.unex.asee.ga02.beergo.database.BeerGoDatabase
import com.unex.asee.ga02.beergo.databinding.FragmentCommentsBinding
import com.unex.asee.ga02.beergo.model.Comment
import com.unex.asee.ga02.beergo.repository.CommentRepository
import com.unex.asee.ga02.beergo.view.viewmodel.BeerViewModel
import com.unex.asee.ga02.beergo.view.viewmodel.UserViewModel
import kotlinx.coroutines.launch


class CommentsFragment: Fragment() {

    private lateinit var db: BeerGoDatabase

    private lateinit var beerViewModel: BeerViewModel
    private lateinit var userViewModel: UserViewModel
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
        beerViewModel = ViewModelProvider(requireActivity()).get(BeerViewModel::class.java)
        userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)

    }

    override fun onAttach(context: android.content.Context) {
        super.onAttach(context)
        db = BeerGoDatabase.getInstance(context)!!
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
        val cerveza = beerViewModel.getSelectedBeer()
        Log.d("ShowBeerFragment", "El id de la cerveza en Comments es ${cerveza?.beerId}")

        val user = userViewModel.getUser()
        Log.d("ShowBeerFragment", "El id del usuario en Comments es ${user.userId}")
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
        val user= userViewModel.getUser()
        val userId = user.userId
        if(userId == userCommentId){
            lifecycleScope.launch {
                db.commentDao().delete(comment)
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
        val beer = beerViewModel.getSelectedBeer()
        beer?.let {
            val idCerveza = it.beerId
            lifecycleScope.launch {
                binding.spinner.visibility = View.VISIBLE
                beerComments = CommentRepository.getInstance(db.commentDao()).loadComments(idCerveza)
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