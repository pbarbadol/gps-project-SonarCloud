package com.unex.asee.ga02.beergo.data.api

import com.google.gson.annotations.SerializedName

class IngredientsAmount (

    @SerializedName("value" ) var value : Double? = null,
    @SerializedName("unit"  ) var unit  : String? = null

)