package com.unex.asee.ga02.beergo.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Beer(
    @PrimaryKey (autoGenerate = true) val beerId: Int,
    val title: String,
    val description: String,
    val year: String,
    val abv: Double,
    val image: String,

    ): Serializable

