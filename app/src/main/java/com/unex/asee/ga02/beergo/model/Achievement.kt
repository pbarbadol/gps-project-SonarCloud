package com.unex.asee.ga02.beergo.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Representa un logro en la aplicación BeerGo.
 *
 * @property achievementId El ID único del logro.
 * @property title El título del logro.
 * @property description La descripción del logro.
 * @property expPoint Puntos de experiencia asociados al logro.
 * @property category La categoría del logro.
 */
@Entity
data class Achievement(
    @PrimaryKey
    var achievementId: Long,
    val title: String,
    val description: String,
    val expPoint: Int,
    val category: String
)
