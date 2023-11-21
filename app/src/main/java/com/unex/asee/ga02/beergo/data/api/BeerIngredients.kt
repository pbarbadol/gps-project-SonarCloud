package com.unex.asee.ga02.beergo.data.api

import com.google.gson.annotations.SerializedName

class BeerIngredients (

    @SerializedName("malt"  ) var malt  : ArrayList<IngredientsMalt> = arrayListOf(),
    @SerializedName("hops"  ) var hops  : ArrayList<IngredientsHops> = arrayListOf(),
    @SerializedName("yeast" ) var yeast : String?         = null

)