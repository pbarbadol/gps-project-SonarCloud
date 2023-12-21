package com.unex.asee.ga02.beergo.data.api

import com.google.gson.annotations.SerializedName

class IngredientsMalt (

    @SerializedName("name"   ) var name   : String? = null,
    @SerializedName("amount" ) var amount : IngredientsAmount? = IngredientsAmount()

)