package com.unex.asee.ga02.beergo.view.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.unex.asee.ga02.beergo.databinding.ListItemListBinding
import com.unex.asee.ga02.beergo.model.Beer

class ListAdapter(
    private val beers: List<Beer>,
    private val onClick: (beer: Beer) -> Unit,
    private val onLongClick: (beer: Beer) -> Unit
) : RecyclerView.Adapter<ListAdapter.ShowBeerHolder>() {
    class ShowBeerHolder(
        private val binding: ListItemListBinding,
        private val onClick: (beer: Beer) -> Unit,
        private val onLongClick: (beer: Beer) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(beer: Beer, totalItems: Int) {
            with(binding) {
                /*
                beerTitle.text = beer.title
                beerNationality.text = beer.nationality
                beerType.text = "${beer.type} types"*/
                itemImg.setImageResource(beer.image)
                clItem.setOnClickListener {
                        onClick(beer)
                }
                clItem.setOnLongClickListener {
                    onLongClick(beer)
                    true
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowBeerHolder {
        val binding =
            ListItemListBinding.inflate(LayoutInflater.from(parent.context),
                parent, false)
        return ShowBeerHolder(binding, onClick, onLongClick)
    }

    override fun getItemCount() = beers.size
    override fun onBindViewHolder(holder: ShowBeerHolder, position: Int) {
            holder.bind(beers[position], beers.size)
        }
}
