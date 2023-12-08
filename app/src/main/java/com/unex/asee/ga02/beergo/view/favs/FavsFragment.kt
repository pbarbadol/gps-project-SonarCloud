package com.unex.asee.ga02.beergo.view.favs

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
import com.unex.asee.ga02.beergo.databinding.FragmentFavsBinding
import com.unex.asee.ga02.beergo.model.Beer
import com.unex.asee.ga02.beergo.view.viewmodel.FavsViewModel
import com.unex.asee.ga02.beergo.view.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

class FavsFragment : Fragment() {

    private val viewModel: FavsViewModel by viewModels { FavsViewModel.Factory }
    private val homeViewModel: HomeViewModel by activityViewModels()

    private lateinit var listener: OnShowClickListener
    interface OnShowClickListener {
        fun onShowClick(beer : Beer)
    }
    private var _binding: FragmentFavsBinding? = null
    private val binding get() = _binding!!
    private lateinit  var adapter: FavsAdapter
    private var favBeers = emptyList<Beer>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onAttach(context: android.content.Context) {
        super.onAttach(context)
        if (context is OnShowClickListener) {
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
        homeViewModel.user.observe(viewLifecycleOwner) { user ->
            Log.d("Observation", "User observed: $user")
            viewModel.user = user
        }
        viewModel.setNoSelectedBeer()
        setUpRecyclerView()
        loadFavourites()
    }
    fun setUpUI() {
    }

    private fun setUpRecyclerView()  {
        adapter = FavsAdapter(beers = favBeers, onClick = {

            val cervezaSeleccionada = viewModel.getSelectedBeer()

            if (cervezaSeleccionada == null) {
                // Si no hay ninguna cerveza seleccionada, establecerla y luego mostrar los detalles
                viewModel.setSelectedBeer(it)
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
        val user = viewModel.getCurrentUser()
        lifecycleScope.launch {
            binding.spinner.visibility = View.VISIBLE
            favBeers = viewModel.loadFavs(user!!.userId)
            adapter.updateData(favBeers)
            binding.spinner.visibility = View.GONE
        }
    }
    private fun deleteBeer(beer: Beer) {
        val user = viewModel.getCurrentUser()
        lifecycleScope.launch {
            viewModel.deleteFav(user!!.userId, beer.beerId)
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