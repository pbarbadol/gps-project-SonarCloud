package com.unex.asee.ga02.beergo.data.api

import com.google.gson.annotations.SerializedName

class MethodFermentation  (

    @SerializedName("temp" ) var temp : MethodFermentationTemp? = MethodFermentationTemp()

)