package com.unex.asee.ga02.beergo.view.history

import History
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.unex.asee.ga02.beergo.databinding.FragmentListHistoryBinding


// Adaptador para la lista de logros
class HistoryAdapter(
    private val historyList: List<History>  ,
    private val onClick: (history: History) -> Unit,
    private val onLongClick: (history: History) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.ShowHistoryHolder>() {

    inner class ShowHistoryHolder(private val binding: FragmentListHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {



        fun bind(historyItem: History) {
            with(binding) {
                beerName.text = historyItem.beer.title
                beerDate.text = historyItem.date.toString()
                Glide.with(beerImage.context)
                    .load(historyItem.beer.image)  // Puedes utilizar una URL o un identificador de recurso según la estructura de tu modelo Beer
                    .into(beerImage)
            }
        }

        init {
            binding.root.setOnClickListener {
                onClick(historyList[adapterPosition])
            }
            binding.root.setOnLongClickListener {
                onLongClick(historyList[adapterPosition])
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowHistoryHolder {
        val binding = FragmentListHistoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ShowHistoryHolder(binding)
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    override fun onBindViewHolder(holder: ShowHistoryHolder, position: Int) {
        holder.bind(historyList[position])
    }
}
