package com.unex.asee.ga02.beergo.view.favs

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.unex.asee.ga02.beergo.databinding.FavsItemListBinding
import com.unex.asee.ga02.beergo.model.Beer

class FavsAdapter (
    private var beers: List<Beer>,
    private val onClick: (beer: Beer) -> Unit,
    private val onLongClick: (beer: Beer) -> Unit,
    private val context: Context?
) : RecyclerView.Adapter<FavsAdapter.ShowFavsBeerHolder>() {
    class ShowFavsBeerHolder(
        private val binding: FavsItemListBinding,
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
                        .into(item2Img)
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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowFavsBeerHolder {
        val binding =
            FavsItemListBinding.inflate(LayoutInflater.from(parent.context),
                parent, false)
        return ShowFavsBeerHolder(binding, onClick, onLongClick,context)
    }

    override fun getItemCount() = beers.size

    override fun onBindViewHolder(holder: ShowFavsBeerHolder, position: Int) {
        holder.bind(beers[position], beers.size)
    }

    fun updateData(newBeers: List<Beer>) {
        beers = newBeers
        notifyDataSetChanged()
    }

}