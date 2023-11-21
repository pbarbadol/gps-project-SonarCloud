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


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavsFragment : Fragment() {

    private lateinit var db: BeerGoDatabase

    private lateinit var listener: OnShowClickListener
    private lateinit var userViewModel: UserViewModel
    interface OnShowClickListener {
        fun onShowClick(beer : Beer)
    }

    private var _binding: FragmentFavsBinding? = null
    private val binding get() = _binding!!
    private lateinit  var adapter: FavsAdapter

    private var favBeers = emptyList<Beer>() //dummyBeers.filter {it.isFavourite}

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
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
        setUpRecyclerView()
        loadFavourites()
    }

    fun setUpUI() {
    }

    private fun setUpRecyclerView()  {
        adapter = FavsAdapter(beers = favBeers, onClick = {

            navigateToShowBeerFragment(it)
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
        val action = FavsFragmentDirections.actionFavsFragmentToShowBeerFragment(beer)
        findNavController().navigate(action)
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
            FavsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}