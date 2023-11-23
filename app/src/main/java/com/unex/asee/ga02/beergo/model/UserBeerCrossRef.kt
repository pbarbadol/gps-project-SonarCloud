package com.unex.asee.ga02.beergo.model

import androidx.room.Entity
import androidx.room.ForeignKey


@Entity(
    primaryKeys = ["userId", "beerId"],
    foreignKeys = [
        ForeignKey(
            entity = Beer::class,
            parentColumns = ["beerId"],
            childColumns = ["beerId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class UserBeerCrossRef(
    val userId: Long,
    val beerId: Int
)
