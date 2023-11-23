package com.unex.asee.ga02.beergo.data.api

import com.google.gson.annotations.SerializedName

class IngredientsHops (

    @SerializedName("name"      ) var name      : String? = null,
    @SerializedName("amount"    ) var amount    : IngredientsAmount? = IngredientsAmount(),
    @SerializedName("add"       ) var add       : String? = null,
    @SerializedName("attribute" ) var attribute : String? = null

)