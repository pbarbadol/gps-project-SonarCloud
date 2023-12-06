package com.unex.asee.ga02.beergo.view.comment

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
import com.unex.asee.ga02.beergo.repository.CommentRepository
import com.unex.asee.ga02.beergo.utils.ChallengeAchievementFunction.ChallengeAchievementObserver
import com.unex.asee.ga02.beergo.view.viewmodel.BeerViewModel
import com.unex.asee.ga02.beergo.view.viewmodel.UserViewModel
import kotlinx.coroutines.launch

class AddCommentFragment: Fragment() {

    private lateinit var db: BeerGoDatabase
    private lateinit var beerViewModel: BeerViewModel
    private lateinit var userViewModel: UserViewModel
    private var _binding: FragmentAddcommentBinding? = null
    private val binding get() = _binding!!
    private lateinit var challengeObserverForCommentTable : ChallengeAchievementObserver

    //Repository
    private lateinit var commentRepository: CommentRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        beerViewModel = ViewModelProvider(requireActivity()).get(BeerViewModel::class.java)
        userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)

        challengeObserverForCommentTable = ChallengeAchievementObserver(userViewModel.getUser(), requireContext(), db)
        db.addDatabaseObserver("Comment", challengeObserverForCommentTable)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        db = BeerGoDatabase.getInstance(context)!!
        commentRepository = CommentRepository.getInstance(db.commentDao())
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
                val comment = Comment(0, idCerv, idUser, comentario, userName)
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
            commentRepository.addComment(comment)
        }
        db.notifyDatabaseObservers("Comment")
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}