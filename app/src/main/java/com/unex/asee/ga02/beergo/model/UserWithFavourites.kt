package com.unex.asee.ga02.beergo.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class UserWithFavourites(
    @Embedded val user: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "beerId",
        associateBy = Junction(UserFavouriteBeerCrossRef::class)
    )
    val beers: List<Beer>
)
