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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.unex.asee.ga02.beergo.databinding.FragmentFavsBinding
import com.unex.asee.ga02.beergo.model.Beer
import com.unex.asee.ga02.beergo.view.viewmodel.FavsViewModel
import com.unex.asee.ga02.beergo.view.viewmodel.HomeViewModel

class FavsFragment : Fragment() {

    private val viewModel: FavsViewModel by viewModels { FavsViewModel.Factory }
    private val homeViewModel: HomeViewModel by activityViewModels()
    private var _binding: FragmentFavsBinding? = null
    private val binding get() = _binding!!
    private lateinit  var adapter: FavsAdapter

    private lateinit var listener: OnShowClickListener
    interface OnShowClickListener {
        fun onShowClick(beer : Beer)
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
        setUpRecyclerView()

        homeViewModel.user.observe(viewLifecycleOwner) { user ->
            Log.d("Observation", "User observed: $user")
            viewModel.user = user
        }
        subscribeUi(adapter)

    }

    fun subscribeUi (adapter: FavsAdapter){
        viewModel.favBeers.observe(viewLifecycleOwner) { favBeers ->
            adapter.updateData(favBeers)
        }
    }
    private fun setUpRecyclerView()  {
        Log.d("Observation", "RECYCLER")
        adapter = FavsAdapter(beers = emptyList(), onClick = {

            homeViewModel.beerInSession = it
            navigateToShowBeerFragment()
        },
            onLongClick = {
                viewModel.deleteBeer(it)
                viewModel.loadFavourites()
                viewModel.toast.observe(viewLifecycleOwner) { text ->
                    text?.let {
                        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
                        viewModel.onToastShown()
                    }
                }
            }, context = context
        )
        with (binding) {
            val numberOfColumns = 2
            rvFavsbeerList.layoutManager = GridLayoutManager(context, numberOfColumns)
            rvFavsbeerList.adapter = adapter
        }
        android.util.Log.d("DiscoverFragment", "setUpRecyclerView")
    }


    //TODO: COMPROBAR CAMBIO
    private fun navigateToShowBeerFragment() {
        findNavController().navigate(FavsFragmentDirections.actionFavsFragmentToShowBeerFragment())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // avoid memory leaks
    }
}