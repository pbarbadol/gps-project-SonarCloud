package com.unex.asee.ga02.beergo.view.list

import History
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.unex.asee.ga02.beergo.R
import com.unex.asee.ga02.beergo.api.APIError
import com.unex.asee.ga02.beergo.api.getNetworkService
import com.unex.asee.ga02.beergo.database.BeerGoDatabase
import com.unex.asee.ga02.beergo.databinding.FragmentListBinding
import com.unex.asee.ga02.beergo.model.Beer
import com.unex.asee.ga02.beergo.repository.BeerRepository
import com.unex.asee.ga02.beergo.repository.FavRepository
import com.unex.asee.ga02.beergo.view.viewmodel.BeerViewModel
import com.unex.asee.ga02.beergo.view.viewmodel.UserViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.Date
import kotlin.collections.*

class ListFragment : Fragment() {
    private var beers: List<Beer> = emptyList()
    private var beersFiltered: List<Beer> = emptyList()
    private var cachedBeers: List<Beer> = emptyList()
    private lateinit var listener: OnShowClickListener
    private lateinit var beerViewModel: BeerViewModel
    private lateinit var db: BeerGoDatabase
    private lateinit var userViewModel: UserViewModel
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ListAdapter
    private lateinit var beerRepository : BeerRepository
    private lateinit var favRepository: FavRepository


    interface OnShowClickListener {
        fun onShowClick(beer: Beer)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Obtenemos el ViewModel de cervecitas
        beerViewModel = ViewModelProvider(requireActivity()).get(BeerViewModel::class.java)
        //Obtenemos el ViewModel de usuario
        userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)

    }

    override fun onAttach(context: android.content.Context) {
        super.onAttach(context)
        //Obtenemos la base de datos
        db = BeerGoDatabase.getInstance(this.requireContext())!!
        beerRepository = BeerRepository.getInstance(db.beerDao(), getNetworkService())
        favRepository = FavRepository.getInstance(db.userDao())
        if (context is OnShowClickListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnShowClickListener")
        }
    }

    private fun subscribeUi (adapter: ListAdapter){
        beerRepository.beers.observe(viewLifecycleOwner) { beers ->
            adapter.updateData(beers)
        }
    }

    private fun launchDataLoad(block: suspend () -> Unit) : Job {
        return lifecycleScope.launch {
            try {
                binding.spinner.visibility = View.VISIBLE
                block()
            } catch (error: APIError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            } finally {
                binding.spinner.visibility = View.GONE
            }
        }
    }

//    private suspend fun mostrarCervezas() {
//        try {
//            //Obtener cervezas de la bd
//            cachedBeers = db.beerDao().getAll()
//
//            //Actualiza la lista de cervezas
//            adapter.updateData(cachedBeers)
//            //Ordena la lista de cervezas por abv
//            adapter.sortByAbv()
//
//        } catch (error: APIError) {
//            Toast.makeText(context, "Error fetching data", Toast.LENGTH_SHORT).show()
//        } finally {
//            withContext(Dispatchers.Main) {
//                binding.spinner.visibility = View.GONE
//            }
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpUI()
        beerViewModel.setSelectedBeer(null)
        setUpRecyclerView()

        subscribeUi(adapter)
        launchDataLoad { beerRepository.tryUpdateRecentBeersCache() }
    }

    private fun setUpUI() {
        // Filtrar las cervezas al cambiar el texto de búsqueda
        binding.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    performSearch(it)
                }
                return true
            }
        })

        // Restaurar la lista original al cerrar el buscador
        binding.searchView.setOnCloseListener {
            adapter.updateData(cachedBeers)
            adapter.notifyDataSetChanged()
            true
        }

        val navController = findNavController()
        binding.btnAddBeer.setOnClickListener {
            navController.navigate(R.id.action_listFragment_to_insertBeerFragment)
        }
    }


    private fun setUpSortingSpinner() {
        val spinnerOpciones = binding.spinnerOpciones
        val listaOpciones = arrayOf("Abv", "Titulo", "Año")

        val adapterSpinner: ArrayAdapter<String> =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listaOpciones)
        spinnerOpciones.adapter = adapterSpinner

        spinnerOpciones.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> adapter.sortByAbv()
                    1 -> adapter.sortByTitle()
                    2 -> adapter.sortByYear()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                adapter.sortByAbv()
            }
        }
    }

    private fun setUpRecyclerView() {
        adapter = ListAdapter(beers = beers, onClick = {
            val cervezaSeleccionada = beerViewModel.getSelectedBeer()
            val history = History(beer = it, date = Date())
            History.saveHistory(history)
            if (cervezaSeleccionada == null) {
                // Si no hay ninguna cerveza seleccionada, establecerla y luego mostrar los detalles
                beerViewModel.setSelectedBeer(it)
                listener.onShowClick(it)
            } else {
                // Si ya hay una cerveza seleccionada, solo mostrar los detalles
                listener.onShowClick(it)
            }
        }, onLongClick = {
            setFavourite(it)
            Toast.makeText(context, "${it.title} añadida a favoritos", Toast.LENGTH_SHORT).show()
        }, context = context
        )

        with(binding) {
            val numberOfColumns = 2
            rvBeerList.layoutManager = GridLayoutManager(context, numberOfColumns)
            rvBeerList.adapter = adapter
        }
        Log.d("DiscoverFragment", "setUpRecyclerView")
    }

    private fun setFavourite(beer: Beer) {
        val user = userViewModel.getUser()
        lifecycleScope.launch {
            if (db != null) {
                favRepository.addFav(user.userId, beer.beerId)
            } else {
                Toast.makeText(context, "Error: Base de datos no disponible", Toast.LENGTH_SHORT)
                    .show()
            }

        }
    }

    private fun performSearch(query: String) {
        beersFiltered = if (query.isNotBlank()) {
            cachedBeers.filter { it.title.contains(query, ignoreCase = true) }
        } else {
            cachedBeers
        }
        adapter.updateData(beersFiltered)
        adapter.notifyDataSetChanged()
        Log.d("ListFragment", "Filtered Beers: $beersFiltered")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}