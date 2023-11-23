package com.unex.asee.ga02.beergo.model

import androidx.room.Entity
import java.util.Date

@Entity(primaryKeys = ["userId", "achievementId"])
data class UserAchievementCrossRef(
    val userId: Long,
    val achievementId: Long,
    val dateObtained: Date //TODO: peligro puede dar error

)