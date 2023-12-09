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
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.unex.asee.ga02.beergo.R
import com.unex.asee.ga02.beergo.databinding.FragmentListBinding
import com.unex.asee.ga02.beergo.model.Beer
import com.unex.asee.ga02.beergo.view.viewmodel.HomeViewModel
import com.unex.asee.ga02.beergo.view.viewmodel.ListViewModel
import java.util.Date
import kotlin.collections.*

class ListFragment : Fragment() {
    //declaracion del ViewModel
    private val viewmodel: ListViewModel by viewModels { ListViewModel.Factory }
    private val homeViewModel: HomeViewModel by activityViewModels()

    private lateinit var listener: OnShowClickListener
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ListAdapter



    interface OnShowClickListener {
        fun onShowClick(beer: Beer)
    }


    override fun onAttach(context: android.content.Context) {
        super.onAttach(context)
        //Obtenemos la base de datos

        if (context is OnShowClickListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnShowClickListener")
        }
    }

    private fun subscribeUi (adapter: ListAdapter){
        viewmodel.beers?.observe(viewLifecycleOwner) { beers ->
            adapter.updateData(beers)
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
        homeViewModel.beerInSession = null
        setUpRecyclerView()

        homeViewModel.user.observe(viewLifecycleOwner) { user ->
            viewmodel.user = user
        }

        viewmodel.spinner.observe(viewLifecycleOwner){show->
                binding.spinner.visibility = if (show) View.VISIBLE else View.GONE
        }
        viewmodel.toast.observe(viewLifecycleOwner){text->
            text?.let {
                Toast.makeText(context, text, Toast.LENGTH_SHORT)
                viewmodel.onToastShown()
            }
        }

        subscribeUi(adapter)

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
                    viewmodel.performSearch(it)
                    adapter.updateData(viewmodel.beersFiltered)
                    adapter.notifyDataSetChanged()
                }
                return true
            }
        })

        // Restaurar la lista original al cerrar el buscador
        binding.searchView.setOnCloseListener {
            adapter.updateData(viewmodel.cachedBeers)
            adapter.notifyDataSetChanged()
            true
        }

        val navController = findNavController()
        binding.btnAddBeer.setOnClickListener {
            navController.navigate(R.id.action_listFragment_to_insertBeerFragment)
        }
    }


    private fun setUpSortingSpinner() {
//        val adapterSpinner: ArrayAdapter<String> =
//            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, arrayOf("Abv", "Titulo", "Año"))
        binding.spinnerOpciones.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, arrayOf("Abv", "Titulo", "Año"))

        binding.spinnerOpciones.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
        adapter = ListAdapter(beers = viewmodel.beers?.value!!, onClick = {
            homeViewModel.beerInSession = it
//            val cervezaSeleccionada = viewmodel.getSelectedBeer()
            History.saveHistory(History(beer = it, date = Date())) //TODO mirar si esto se hace así o no
            if (homeViewModel.isNull()) {
                // Si no hay ninguna cerveza seleccionada, establecerla y luego mostrar los detalles
                homeViewModel.beerInSession = it
                listener.onShowClick(it)
            } else {
                // Si ya hay una cerveza seleccionada, solo mostrar los detalles
                listener.onShowClick(it)
            }
        }, onLongClick = {

            viewmodel.setFavourite(it)
        }, context = context
        )

        with(binding) {
            rvBeerList.layoutManager = GridLayoutManager(context, 2)
            rvBeerList.adapter = adapter
        }
        Log.d("DiscoverFragment", "setUpRecyclerView")
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}