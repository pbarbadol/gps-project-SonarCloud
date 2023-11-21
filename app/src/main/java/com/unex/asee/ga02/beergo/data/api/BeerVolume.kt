package com.unex.asee.ga02.beergo.data.api

import com.google.gson.annotations.SerializedName

class BeerVolume (
    @SerializedName("value" ) var value : Int?    = null,
    @SerializedName("unit"  ) var unit  : String? = null

    )