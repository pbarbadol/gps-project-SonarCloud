package com.unex.asee.ga02.beergo.view.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.unex.asee.ga02.beergo.R
import com.unex.asee.ga02.beergo.database.BeerGoDatabase
import com.unex.asee.ga02.beergo.databinding.FragmentAddcommentBinding
import com.unex.asee.ga02.beergo.model.Comment
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.launch


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AddCommentFragment: Fragment() {

    private lateinit var db: BeerGoDatabase

    private lateinit var beerViewModel: BeerViewModel
    private lateinit var userViewModel: UserViewModel

    private var _binding: FragmentAddcommentBinding? = null
    private val binding get() = _binding!!

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        db = BeerGoDatabase.getInstance(context)!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddcommentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        beerViewModel = ViewModelProvider(requireActivity()).get(BeerViewModel::class.java)
        userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)

        val beer = beerViewModel.getSelectedBeer()
        val user = userViewModel.getUser()
        Log.d("AddCommentFragment","El id de la cerveza en AddCommentFragment es ${beer?.beerId}")
        Log.d("AddCommentFragment","El id del usuario en AddCommentFragment es ${user.userId}")


        val contenido = view.findViewById<EditText>(R.id.editTextText)

        val btAccept = binding.btAccept
        btAccept.setOnClickListener {
            val comentario = contenido.text.toString().trim()

            if(comentario.isNotEmpty()){
                val idCerv = beer!!.beerId
                val idUser = user.userId
                val userName = user.name
                val comment = Comment(null, idCerv, idUser, comentario, userName)
                addComment(comment)
            }
            findNavController().popBackStack()
        }

        val btCancel = binding.btCancel
        btCancel.setOnClickListener {
            findNavController().popBackStack()
        }



    }

    private fun addComment(comment: Comment){
        lifecycleScope.launch {
            db.commentDao().insert(comment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AchievementsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddCommentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}