package com.unex.asee.ga02.beergo.view.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.unex.asee.ga02.beergo.api.APICallback
import com.unex.asee.ga02.beergo.api.APIError
import com.unex.asee.ga02.beergo.api.getNetworkService
import com.unex.asee.ga02.beergo.data.api.BeerApi
//import com.unex.asee.ga02.beergo.data.dummyBeers
import com.unex.asee.ga02.beergo.data.toBeer
import com.unex.asee.ga02.beergo.databinding.FragmentListBinding
import com.unex.asee.ga02.beergo.model.Beer
import com.unex.asee.ga02.beergo.utils.BACKGROUND
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListFragment : Fragment() {

    private var _beers: List<Beer> = emptyList()
    private lateinit var listener: OnShowClickListener

    interface OnShowClickListener {
        fun onShowClick(beer: Beer)
    }

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ListAdapter

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()

        if (_beers.isEmpty()) {
            binding.spinner.visibility = View.VISIBLE

            lifecycleScope.launch {
                if (_beers.isEmpty()) {
                    binding.spinner.visibility = View.VISIBLE
                    try {
                        _beers = fetchBeers()
                        adapter.updateData(_beers)

                    } catch (error: APIError) {
                        Toast.makeText(context, "Error fetching data", Toast.LENGTH_SHORT).show()
                    } finally {
                        binding.spinner.visibility = View.GONE
                    }
                }
            }
        }
    }


    private suspend fun fetchBeers(): List<Beer> {

        return withContext(Dispatchers.IO) {
            try {
                val result = getNetworkService().getBeers(1).execute()

                if (result.isSuccessful) {
                    val responseBody = result.body()

                    if (responseBody != null) {
                        return@withContext responseBody.map { it?.toBeer() ?: Beer(0,""," "," ",0.0,"") }
                    } else {
                        throw Exception("Response body is null")
                    }
                } else {
                    throw Exception("Error: ${result.code()} ${result.message()}")
                }
            } catch (e: Exception) {
                // Manejar la excepción apropiadamente, puedes imprimir el mensaje o lanzar otra excepción.
                e.printStackTrace()
                throw e
            }
        }



    }


    fun setUpUI() {
    }

    private fun setUpRecyclerView() {
        adapter = ListAdapter(beers = _beers, onClick = {

            listener.onShowClick(it)
        },
            onLongClick = {
                Toast.makeText(context, "long click on: " + it.title, Toast.LENGTH_SHORT).show()
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
                ListFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
        }
    }