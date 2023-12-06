package com.unex.asee.ga02.beergo.view.detail
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.unex.asee.ga02.beergo.database.BeerGoDatabase
import com.unex.asee.ga02.beergo.databinding.FragmentShowBeerBinding
import com.unex.asee.ga02.beergo.repository.FavRepository
import com.unex.asee.ga02.beergo.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.unex.asee.ga02.beergo.utils.ChallengeAchievementFunction.ChallengeAchievementObserver
import com.unex.asee.ga02.beergo.view.viewmodel.BeerViewModel
import com.unex.asee.ga02.beergo.view.viewmodel.UserViewModel
class ShowBeerFragment : Fragment() {
    private lateinit var db: BeerGoDatabase
    private var _binding: FragmentShowBeerBinding? = null
    private lateinit var userViewModel: UserViewModel
    private lateinit var beerViewModel: BeerViewModel
    private val binding get() = _binding!!
    private lateinit var challengeObserverForUserFavouriteBeerCrossRefTable : ChallengeAchievementObserver

    //Repositorios
    private lateinit var favRepository: FavRepository //TODO: hacer esto para todos los repostiorios y fragmentos
    private lateinit var userRepository: UserRepository
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
        favRepository = FavRepository.getInstance(db.userDao())
        userRepository = UserRepository.getInstance(db.userDao())
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onViewCreated(View: View, savedInstanceState: Bundle?) { //TODO: reparar visualización de la imagen
        super.onViewCreated(View, savedInstanceState)
        val beer = beerViewModel.getSelectedBeer()
        if (beer == null) {
            findNavController().popBackStack() //Retrocedemos en la navegación
            return
        }
        val inserted = beer.insertedBy
        if(inserted != null){
            lifecycleScope.launch(Dispatchers.Main) {
                val userId = beer.insertedBy
                val user = userRepository.getUser(userId)
                if(user != null) {
                    val userName = user.name
                    binding.nombreUsuario.text = "Insertada por " + userName
                }
            }
        }
        binding.id.text = beer!!.beerId.toString()
        binding.title.text = beer.title
        binding.anio.text = beer.year
        binding.description.text = beer.description
        binding.abv.text = beer.abv.toString() + "%"
        Glide.with(this)
            .load(beer.image)
            .into(binding.beerImage)
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
                        favRepository.addFav(user.userId, beerId)
                        //db.notifyDatabaseObservers("UserFavouriteBeerCrossRef") TODO: observer, arreglar
                    } else {
                        favRepository.deleteFav(user.userId!!, beerId)
                    }
                }
            }
        }
    }

    /**
     * Comprueba si la cerveza está en la lista de favoritos del usuario
     * @return true si está en la lista de favoritos, false en caso contrario
     */
    private suspend fun isInFavourite(id: Long): Boolean {
        return withContext(Dispatchers.IO) {
            val user = userViewModel.getUser()
            favRepository.isFavorite(user.userId, id)
        }
    }

}