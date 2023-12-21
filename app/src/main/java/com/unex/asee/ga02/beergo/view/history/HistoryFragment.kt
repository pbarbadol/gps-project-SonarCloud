package com.unex.asee.ga02.beergo.view.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.unex.asee.ga02.beergo.databinding.FragmentHistoryBinding
import com.unex.asee.ga02.beergo.view.history.HistoryAdapter
import com.unex.asee.ga02.beergo.view.viewmodel.HomeViewModel

class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null

    private val binding get() = _binding!!
    private lateinit var adapter: HistoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Crear un adaptador para la lista de logros
        adapter = HistoryAdapter(
            History.getHistoryList(),
            { history ->
                Toast.makeText(context, "Clic corto en ${history.beer.title}", Toast.LENGTH_SHORT)
                    .show()
            }
        ) { history ->
            Toast.makeText(context, "Clic largo en ${history.beer.title}", Toast.LENGTH_SHORT)
                .show()
        }
        // Configurar el RecyclerView con el adaptador
        with(binding) {
            rvHistory.layoutManager = LinearLayoutManager(context)
            rvHistory.adapter = adapter
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Evitar fugas de memoria
    }
}