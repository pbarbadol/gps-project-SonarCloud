package com.unex.asee.ga02.beergo.model

import androidx.room.Entity
import androidx.room.ForeignKey
import java.time.LocalDate

/**
 * La entidad `UserAchievementCrossRef` representa la relación muchos a muchos entre las entidades `User` y `Achievement`
 * en la aplicación BeerGo, guardando la fecha en que un usuario obtuvo un logro específico.
 *
 * @property userId Identificador del usuario asociado a la relación.
 * @property achievementId Identificador del logro asociado a la relación.
 * @property date Fecha en que el usuario obtuvo el logro.
 */
@Entity(
    primaryKeys = ["userId", "achievementId"],
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Achievement::class,
            parentColumns = ["achievementId"],
            childColumns = ["achievementId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class UserAchievementCrossRef(
    val userId: Long,
    val achievementId: Long
    //,val date: LocalDate TODO: Implementar fecha
)
