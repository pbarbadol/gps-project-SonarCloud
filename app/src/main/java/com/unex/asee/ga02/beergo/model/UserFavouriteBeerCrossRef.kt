package com.unex.asee.ga02.beergo.model

import androidx.room.Entity
import androidx.room.ForeignKey

/**
 * La entidad `UserFavouriteBeerCrossRef` representa la relación muchos a muchos entre las entidades `User` y `Beer`
 * en la aplicación BeerGo, indicando que un usuario ha marcado una cerveza como favorita.
 *
 * @property userId Identificador del usuario asociado a la relación.
 * @property beerId Identificador de la cerveza asociada a la relación.
 */
@Entity(
    primaryKeys = ["userId", "beerId"],
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Beer::class,
            parentColumns = ["beerId"],
            childColumns = ["beerId"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class UserFavouriteBeerCrossRef(
    val userId: Long,
    val beerId: Int
)
