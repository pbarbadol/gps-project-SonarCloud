package com.unex.asee.ga02.beergo.view.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.unex.asee.ga02.beergo.R
import com.unex.asee.ga02.beergo.data.api.BeerApi
import com.unex.asee.ga02.beergo.databinding.ListItemListBinding
import com.unex.asee.ga02.beergo.model.Beer


class ListAdapter(
    private var beers: List<Beer>,
    private val onClick: (beer: Beer) -> Unit,
    private val onLongClick: (beer: Beer) -> Unit,
    private val context: Context?
) : RecyclerView.Adapter<ListAdapter.ShowBeerHolder>() {
    class ShowBeerHolder(
        private val binding: ListItemListBinding,
        private val onClick: (beer: Beer) -> Unit,
        private val onLongClick: (beer: Beer) -> Unit,
        private val context: Context?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(beer: Beer, totalItems: Int) {
            with(binding) {


                title.text = beer.title
                abv.text = beer.abv.toString()
                year.text = beer.year.toString()



                context
                context?.let {
                    Glide.with(context)
                        .load(beer.image)
                        .into(itemImg)
                }

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
        return ShowBeerHolder(binding, onClick, onLongClick,context)
    }

    fun sortByAbv() {
        beers = beers.sortedBy { it.abv }
        notifyDataSetChanged()

    }
    fun sortByTitle() {
        beers = beers.sortedBy { it.title }
        notifyDataSetChanged()

    }
    fun sortByYear() {
        beers = beers.sortedBy { it.year }
        notifyDataSetChanged()

    }

    fun updateData(beers: List<Beer>) {
        this.beers = beers
        notifyDataSetChanged()
    }
    override fun getItemCount() = beers.size
    override fun onBindViewHolder(holder: ShowBeerHolder, position: Int) {
            holder.bind(beers[position], beers.size)
        }
}
