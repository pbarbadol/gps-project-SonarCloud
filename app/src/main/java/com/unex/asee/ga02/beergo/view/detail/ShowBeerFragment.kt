package com.unex.asee.ga02.beergo.view.detail
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.unex.asee.ga02.beergo.BeerGoApplication
import com.unex.asee.ga02.beergo.database.BeerGoDatabase
import com.unex.asee.ga02.beergo.databinding.FragmentShowBeerBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
//import com.unex.asee.ga02.beergo.utils.ChallengeAchievementFunction.ChallengeAchievementObserver
import com.unex.asee.ga02.beergo.view.viewmodel.ShowBeerViewModel

class ShowBeerFragment : Fragment() {
    private val viewModel: ShowBeerViewModel by viewModels { ShowBeerViewModel.Factory }
    private lateinit var db: BeerGoDatabase
    private var _binding: FragmentShowBeerBinding? = null

    private val binding get() = _binding!!
//    private lateinit var challengeObserverForUserFavouriteBeerCrossRefTable : ChallengeAchievementObserver


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var user = viewModel.getCurrentUser()!!
//        challengeObserverForUserFavouriteBeerCrossRefTable = ChallengeAchievementObserver(user, requireContext(), db)
//        db.addDatabaseObserver("UserFavouriteBeerCrossRef", challengeObserverForUserFavouriteBeerCrossRefTable)
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

        //Obtenemos los datos del contenedor de dependencias de la aplicacion
        val appContainer = (this.activity?.application as BeerGoApplication).appContainer
        db = appContainer.db!!

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onViewCreated(View: View, savedInstanceState: Bundle?) { //TODO: reparar visualización de la imagen
        super.onViewCreated(View, savedInstanceState)
        val beer = viewModel.getSelectedBeer()
        if (beer == null) {
            findNavController().popBackStack() //Retrocedemos en la navegación
            return
        }
        val inserted = beer.insertedBy
        if(inserted != null){
            lifecycleScope.launch(Dispatchers.Main) {
                val userId = beer.insertedBy
                val user = viewModel.getUser(userId)
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
            val isFavourite: Boolean = viewModel.isInFavourite(beerId)
            binding.favSwitch.isChecked = isFavourite
            binding.favSwitch.setOnCheckedChangeListener { _, isChecked ->
                val user = viewModel.getCurrentUser()
                lifecycleScope.launch(Dispatchers.IO) {
                    if (isChecked) {
                        viewModel.addFav(user!!.userId, beerId)
                        //db.notifyDatabaseObservers("UserFavouriteBeerCrossRef") TODO: observer, arreglar
                    } else {
                        viewModel.deleteFav(user!!.userId, beerId)
                    }
                }
            }
        }
    }




}