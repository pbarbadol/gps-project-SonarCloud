package com.unex.asee.ga02.beergo.model
import java.io.Serializable

data class User(
    val name: String = "",
    val password: String = ""
) : Serializable
