package com.unex.asee.ga02.beergo.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable

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

data class Comment(
    @PrimaryKey(autoGenerate = true) var commentId: Long?,
    val beerId: Int,
    val userId: Int,
    val comment: String

): Serializable