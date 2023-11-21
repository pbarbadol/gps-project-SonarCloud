package com.unex.asee.ga02.beergo.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Achievement(
    @PrimaryKey var achievementId: Long?,
    val title: String,
    val description: String,
    val category: String,
    val expPoints: Int,

    ): Serializable