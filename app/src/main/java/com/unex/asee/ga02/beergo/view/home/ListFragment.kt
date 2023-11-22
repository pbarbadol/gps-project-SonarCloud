package com.unex.asee.ga02.beergo.view.home

import History
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.unex.asee.ga02.beergo.R
import com.unex.asee.ga02.beergo.api.APIError
import com.unex.asee.ga02.beergo.api.getNetworkService
import com.unex.asee.ga02.beergo.data.api.BeerApi
//import com.unex.asee.ga02.beergo.data.dummyBeers
import com.unex.asee.ga02.beergo.data.toBeer
import com.unex.asee.ga02.beergo.database.BeerGoDatabase
import com.unex.asee.ga02.beergo.databinding.FragmentListBinding
import com.unex.asee.ga02.beergo.model.Beer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ListFragment : Fragment() {

    private var beers: List<Beer> = emptyList()
    private var beersFiltered: List<Beer> = emptyList()
    private var cachedBeers: List<Beer> = emptyList()
    private lateinit var listener: OnShowClickListener
    private lateinit var beerViewModel: BeerViewModel

    private lateinit var db: BeerGoDatabase
    private lateinit var userViewModel: UserViewModel
    interface OnShowClickListener {
        fun onShowClick(beer: Beer)
    }

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ListAdapter

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
        if (context is OnShowClickListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnShowClickListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUI()
        setUpRecyclerView()

        beerViewModel.setSelectedBeer(null)

        binding.spinner.visibility = View.VISIBLE
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                cachedBeers = fetchBeers()

                withContext(Dispatchers.Main) {
                    adapter.updateData(cachedBeers)
                    for (beer in cachedBeers) {
                        db.beerDao().insert(beer)
                    }
                    Log.d("ListFragment", "El tamaño de _beers despues de actualizar es: ${beers.size}")
                }
            } catch (error: APIError) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error fetching data", Toast.LENGTH_SHORT).show()
                }
            } finally {
                withContext(Dispatchers.Main) {
                    binding.spinner.visibility = View.GONE
                }
            }
        }
    }



    private suspend fun fetchBeers(): List<Beer> = withContext(Dispatchers.IO) {
        try {
            val result = getNetworkService().getBeers(1).execute()

            if (result.isSuccessful) {
                result.body()?.map { it?.toBeer() ?: Beer(0, "", " ", " ", 0.0, "") }
                    ?: throw Exception("Response body is null")
            } else {
                throw Exception("Error: ${result.code()} ${result.message()}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }



    fun setUpUI() {
    }

    private fun setUpRecyclerView() {
        adapter = ListAdapter(beers = beers, onClick = {

            val cervezaSeleccionada = beerViewModel.getSelectedBeer()
            val history = History( beer = it, date = Date())
            History.saveHistory(history)
            if (cervezaSeleccionada == null) {
                // Si no hay ninguna cerveza seleccionada, establecerla y luego mostrar los detalles
                beerViewModel.setSelectedBeer(it)
                listener.onShowClick(it)
            } else {
                // Si ya hay una cerveza seleccionada, solo mostrar los detalles
                listener.onShowClick(it)
            }

        },
            onLongClick = {
                setFavourite(it)
                Toast.makeText(context, "${it.title} añadida a favoritos", Toast.LENGTH_SHORT).show()
            }
            , context = context
        )
        with (binding) {
            val numberOfColumns = 2
            rvBeerList.layoutManager = GridLayoutManager(context, numberOfColumns)
            rvBeerList.adapter = adapter
        }
        android.util.Log.d("DiscoverFragment", "setUpRecyclerView")
    }

    private fun setFavourite(beer: Beer) {
        val user = userViewModel.getUser()
        lifecycleScope.launch {
            if (db != null) {
                db.beerDao().insertAndRelate(beer, user.userId!!)
            } else {
                Toast.makeText(context, "Error: Base de datos no disponible", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun performSearch(query: String) {
        var beersFiltered = cachedBeers.filter { it.title.contains(query, ignoreCase = true) }
        adapter.updateData(beersFiltered)
        adapter.notifyDataSetChanged()
        Log.d("ListFragment", "Filtered Beers: $beersFiltered")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // avoid memory leaks
    }
    override fun onResume() {
        super.onResume()

        // Actualiza la lista de cervezas al volver al fragmento
        if (cachedBeers.isNotEmpty()) {
            adapter.updateData(cachedBeers)
        }
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}