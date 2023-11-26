package com.unex.asee.ga02.beergo.view.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import android.widget.Button
import android.widget.ImageView
import androidx.navigation.fragment.findNavController

import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.unex.asee.ga02.beergo.api.APIError
import com.unex.asee.ga02.beergo.api.getNetworkService
import com.unex.asee.ga02.beergo.data.api.BeerApi
import com.unex.asee.ga02.beergo.R
import com.unex.asee.ga02.beergo.database.BeerGoDatabase
import com.unex.asee.ga02.beergo.databinding.FragmentListBinding
import com.unex.asee.ga02.beergo.databinding.FragmentShowBeerBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.unex.asee.ga02.beergo.model.Beer
import com.unex.asee.ga02.beergo.model.UserFavouriteBeerCrossRef
import com.unex.asee.ga02.beergo.utils.ChallengeAchievementFunction.ChallengeAchievementObserver

class ShowBeerFragment : Fragment() {

    private lateinit var db: BeerGoDatabase

    private var _binding: FragmentShowBeerBinding? = null

    private lateinit var userViewModel: UserViewModel
    private lateinit var beerViewModel: BeerViewModel
    private val binding get() = _binding!!

    private lateinit var challengeObserverForUserFavouriteBeerCrossRefTable : ChallengeAchievementObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        beerViewModel = ViewModelProvider(requireActivity()).get(BeerViewModel::class.java)
        challengeObserverForUserFavouriteBeerCrossRefTable = ChallengeAchievementObserver(userViewModel.getUser(), requireContext(), db)
        db.addDatabaseObserver("UserFavouriteBeerCrossRef", challengeObserverForUserFavouriteBeerCrossRefTable)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentShowBeerBinding.inflate(inflater, container, false)
        return binding.root
    }

    //Se crea la funcion onAttach para poder inicializar correctamente la instancia de la base de datos
    override fun onAttach(context: Context) {
        super.onAttach(context)
        db = BeerGoDatabase.getInstance(context)!!
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // avoid memory leaks
    }

    override fun onViewCreated(View: View, savedInstanceState: Bundle?) {
        super.onViewCreated(View, savedInstanceState)

        val beer = beerViewModel.getSelectedBeer()
        binding.id.text = beer!!.beerId.toString()
        binding.title.text = beer.title
        binding.anio.text = beer.year
        binding.description.text = beer.description
        binding.abv.text = beer.abv.toString() + "%"
        Glide.with(this)
            .load(beer.image)
            .into(binding.beerImage)
        //binding.type.text = beer.type
        //binding.type2.text = beer.type
        //binding.type3.text = beer.type
        //binding.beerImage.setImageResource(beer.image)

        //Navegación a AddCommentFragment
        val imageView7 = binding.imageView7
        imageView7.setOnClickListener {
            val navController = findNavController()
            val action = ShowBeerFragmentDirections.actionShowBeerFragmentToCommentsFragment()
            navController.navigate(action)
        }
        val beerId = beer.beerId

        lifecycleScope.launch(Dispatchers.Main) {
            val isFavourite: Boolean = isInFavourite(beerId)
            binding.favSwitch.isChecked = isFavourite

            binding.favSwitch.setOnCheckedChangeListener { _, isChecked ->
                val user = userViewModel.getUser()
                lifecycleScope.launch(Dispatchers.IO) {
                    if (isChecked) {
                        db.beerDao().insertAndRelate(beer, user.userId!!)
                        db.notifyDatabaseObservers("UserFavouriteBeerCrossRef")
                    } else {
                        db.beerDao().deleteAndRelate(beer, user.userId!!)
                    }
                }
            }
        }


    }

    suspend fun isInFavourite(id: Int): Boolean {
        return withContext(Dispatchers.IO) {
            db.beerDao().isBeerInFavorites(id) > 0
        }
    }
    private fun beerBinding(beer: Beer){
        binding.id.text = beer.beerId.toString()
        binding.title.text = beer.title
        binding.description.text = beer.description
        binding.abv.text = beer.abv.toString()
        Glide.with(this)
            .load(beer.image)
            .into(binding.beerImage)
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        db.removeDatabaseObserver("UserFavouriteBeerCrossRef", challengeObserverForUserFavouriteBeerCrossRefTable)
//    }
}