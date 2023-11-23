package com.unex.asee.ga02.beergo.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.ForeignKey
import java.util.Date


@Entity(
    primaryKeys = ["userId", "achievementId"], foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["userId"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    ), ForeignKey(
        entity = Achievement::class,
        parentColumns = ["achievementId"],
        childColumns = ["achievementId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class UserAchievementCrossRef(
    @NonNull val userId: Long,
    @NonNull val achievementId: Long,

)
