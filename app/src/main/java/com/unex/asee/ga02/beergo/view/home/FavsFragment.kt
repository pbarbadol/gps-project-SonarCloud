package com.unex.asee.ga02.beergo.view.home

import android.os.Bundle
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
import com.unex.asee.ga02.beergo.databinding.FragmentFavsBinding
import com.unex.asee.ga02.beergo.databinding.FragmentListBinding
import com.unex.asee.ga02.beergo.model.Beer
import com.unex.asee.ga02.beergo.model.UserFavouriteBeerCrossRef
import kotlinx.coroutines.launch

class FavsFragment : Fragment() {

    private lateinit var db: BeerGoDatabase

    private lateinit var listener: OnShowClickListener
    private lateinit var beerViewModel: BeerViewModel
    private lateinit var userViewModel: UserViewModel
    interface OnShowClickListener {
        fun onShowClick(beer : Beer)
    }

    private var _binding: FragmentFavsBinding? = null
    private val binding get() = _binding!!
    private lateinit  var adapter: FavsAdapter

    private var favBeers = emptyList<Beer>() //dummyBeers.filter {it.isFavourite}



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        beerViewModel = ViewModelProvider(requireActivity()).get(BeerViewModel::class.java)

    }

    override fun onAttach(context: android.content.Context) {
        super.onAttach(context)

        db = BeerGoDatabase.getInstance(context)!!

        if (context is FavsFragment.OnShowClickListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnShowClickListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(View: View, savedInstanceState: Bundle?) {
        super.onViewCreated(View, savedInstanceState)
        beerViewModel.setSelectedBeer(null)
        setUpRecyclerView()
        loadFavourites()
    }

    fun setUpUI() {
    }

    private fun setUpRecyclerView()  {
        adapter = FavsAdapter(beers = favBeers, onClick = {

            val cervezaSeleccionada = beerViewModel.getSelectedBeer()

            if (cervezaSeleccionada == null) {
                // Si no hay ninguna cerveza seleccionada, establecerla y luego mostrar los detalles
                beerViewModel.setSelectedBeer(it)
                navigateToShowBeerFragment(it)
            } else {
                // Si ya hay una cerveza seleccionada, solo mostrar los detalles
                navigateToShowBeerFragment(it)
            }
        },
            onLongClick = {
                deleteBeer(it)
                loadFavourites()
                Toast.makeText(context, "${it.title} eliminada de favoritos", Toast.LENGTH_SHORT).show()
            }, context = context
        )
        with (binding) {
            val numberOfColumns = 2
            rvFavsbeerList.layoutManager = GridLayoutManager(context, numberOfColumns)
            rvFavsbeerList.adapter = adapter
        }
        android.util.Log.d("DiscoverFragment", "setUpRecyclerView")
    }

    private fun loadFavourites(){
        val user = userViewModel.getUser()
        lifecycleScope.launch {
            binding.spinner.visibility = View.VISIBLE
            favBeers = db.beerDao().getUserWithFavourites(user.userId!!).beers
            adapter.updateData(favBeers)
            binding.spinner.visibility = View.GONE
        }
    }

    private fun deleteBeer(beer: Beer) {
        val user = userViewModel.getUser()
        lifecycleScope.launch {
            db.beerDao().deleteAndRelate(beer, user.userId!!)
        }
    }
    private fun navigateToShowBeerFragment(beer: Beer) {
        val action = FavsFragmentDirections.actionFavsFragmentToShowBeerFragment()
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // avoid memory leaks
    }
}