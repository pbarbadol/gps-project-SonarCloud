package com.unex.asee.ga02.beergo.data.api

import com.google.gson.annotations.SerializedName

class BeerMethod (
    @SerializedName("mash_temp"    ) var mashTemp     : ArrayList<MethodMashTemp> = arrayListOf(),
    @SerializedName("fermentation" ) var fermentation : MethodFermentation?       = MethodFermentation(),
    @SerializedName("twist"        ) var twist        : String?             = null

    )