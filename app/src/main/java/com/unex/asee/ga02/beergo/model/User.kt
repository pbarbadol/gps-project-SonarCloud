package com.unex.asee.ga02.beergo.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * La entidad `User` representa a un usuario en la aplicación BeerGo.
 *
 * @property userId Identificador único autoincremental del usuario.
 * @property name El nombre del usuario.
 * @property password La contraseña del usuario.
 */
@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    var userId: Long,
    val name: String = "",
    val password: String = ""
) : Serializable
