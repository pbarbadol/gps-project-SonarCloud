package com.unex.asee.ga02.beergo.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

/**
 * La clase `UserWithFavourites` representa la relación entre un usuario y sus cervezas favoritas en la aplicación BeerGo.
 *
 * @property user Objeto `User` incrustado que contiene la información del usuario.
 * @property beers Lista de cervezas favoritas asociadas al usuario.
 */
data class UserWithFavourites(
    // @Embedded se utiliza para indicar que el objeto User debe ser "incrustado" en esta clase.
    @Embedded val user: User,

    // @Relation se utiliza para definir la relación entre User y Beer.
    // parentColumn especifica la columna en la tabla User que se asocia con la entidad principal.
    // entityColumn especifica la columna en la entidad secundaria (Beer) que se asocia con la entidad principal.
    // associateBy establece la relación a través de la tabla de cruce (UserFavouriteBeerCrossRef).
    @Relation(
        parentColumn = "userId",
        entityColumn = "beerId",
        associateBy = Junction(UserFavouriteBeerCrossRef::class)
    )
    val beers: List<Beer> // Lista de cervezas favoritas asociadas a un usuario.
)
