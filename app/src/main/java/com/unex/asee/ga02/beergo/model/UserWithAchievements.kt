package com.unex.asee.ga02.beergo.model

import androidx.lifecycle.LiveData
import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

/**
 * La clase `UserWithAchievements` representa la relación entre un usuario y sus logros en la aplicación BeerGo.
 *
 * @property user Objeto User incrustado que contiene la información del usuario.
 * @property achievements Lista de logros asociados al usuario.
 */
data class UserWithAchievements(
    // @Embedded se utiliza para indicar que el objeto User debe ser "incrustado" en esta clase.
    @Embedded val user: User,

    // @Relation se utiliza para definir la relación entre User y Achievement.
    // parentColumn especifica la columna en la tabla User que se asocia con la entidad principal.
    // entityColumn especifica la columna en la entidad secundaria (Achievement) que se asocia con la entidad principal.
    // associateBy establece la relación a través de la tabla de cruce (UserAchievementCrossRef).
    @Relation(
        parentColumn = "userId",
        entityColumn = "achievementId",
        associateBy = Junction(UserAchievementCrossRef::class)
    ) val achievements: List<Achievement> // Lista de logros asociados a un usuario.
)