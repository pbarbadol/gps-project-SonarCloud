package com.unex.asee.ga02.beergo.view.home

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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.unex.asee.ga02.beergo.database.BeerGoDatabase
import com.unex.asee.ga02.beergo.databinding.FragmentCommentsBinding
import com.unex.asee.ga02.beergo.databinding.FragmentListBinding
import com.unex.asee.ga02.beergo.model.Comment
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

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

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        beerViewModel = ViewModelProvider(requireActivity()).get(BeerViewModel::class.java)
        userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
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
        Log.d("ShowBeerFragment","El id de la cerveza en Comments es ${cerveza?.beerId}")

        val user = userViewModel.getUser()
        Log.d("ShowBeerFragment","El id del usuario en Comments es ${user.userId}")
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
        android.util.Log.d("DiscoverFragment", "setUpRecyclerView")
    }

    private fun deleteComment(comment: Comment) {
        val userCommentId = comment.userId
        val user= userViewModel.getUser()
        val userId = user.userId
        if(userId == userCommentId){
            lifecycleScope.launch {
                db.commentDao().delete(comment)
                Toast.makeText(requireContext(), "Se ha borrado el comentario con ID: ${comment.commentId}",Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "No eres el creador de este comentario",Toast.LENGTH_SHORT).show()
        }

    }

    private fun loadComments() {
        val beer = beerViewModel.getSelectedBeer()
        beer?.let {
            val idCerveza = it.beerId
            lifecycleScope.launch {
                binding.spinner.visibility = View.VISIBLE
                beerComments = db.commentDao().findByBeer(idCerveza)
                adapter.updateData(beerComments)
                binding.spinner.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // avoid memory leaks
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ListFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CommentsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}