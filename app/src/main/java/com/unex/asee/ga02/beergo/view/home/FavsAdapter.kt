package com.unex.asee.ga02.beergo.view.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
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
                /*
                beerTitle.text = beer.title
                beerNationality.text = beer.nationality
                beerType.text = "${beer.type} types"*/
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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavsAdapter.ShowFavsBeerHolder {
        val binding =
            FavsItemListBinding.inflate(LayoutInflater.from(parent.context),
                parent, false)
        return ShowFavsBeerHolder(binding, onClick, onLongClick,context)
    }

    override fun getItemCount() = beers.size

    override fun onBindViewHolder(holder: FavsAdapter.ShowFavsBeerHolder, position: Int) {
        holder.bind(beers[position], beers.size)
    }

    fun updateData(newBeers: List<Beer>) {
        beers = newBeers
        notifyDataSetChanged()
    }

}