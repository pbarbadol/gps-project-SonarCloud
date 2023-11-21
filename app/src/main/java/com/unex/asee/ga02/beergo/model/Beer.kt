package com.unex.asee.ga02.beergo.model


import java.io.Serializable

data class Beer(
    val id: Int,
    val title: String,
    val description: String,
    val year: String,
    val abv: Double,
    val image: String,

    ): Serializable

