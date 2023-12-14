package com.unex.asee.ga02.beergo.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.unex.asee.ga02.beergo.model.Achievement
import com.unex.asee.ga02.beergo.model.UserAchievementCrossRef
import com.unex.asee.ga02.beergo.model.UserWithAchievements

/**
 * Data Access Object (DAO) para la entidad Achievement.
 */
@Dao
interface AchievementDao {

    /**
     * Obtiene todos los logros.
     *
     * @return Lista de [Achievement].
     */
    @Query("SELECT * FROM Achievement")
    fun getAll(): LiveData<List<Achievement>>

    /**
     * Obtiene un logro por su ID.
     *
     * @param id El ID del logro.
     * @return El [Achievement] encontrado.
     */
    @Query("SELECT * FROM Achievement WHERE AchievementId = :id")
    suspend fun findById(id: Int): Achievement

    /**
     * Inserta o reemplaza un logro.
     *
     * @param achievement El logro a insertar o reemplazar.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(achievement: Achievement)

    /**
     * Elimina un logro.
     *
     * @param achievement El logro a eliminar.
     */
    @Delete
    suspend fun delete(achievement: Achievement)
    /**
     * Inserta una relación entre usuario y logro.
     *
     * @param crossRef La relación a insertar.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUserAchievement(crossRef: UserAchievementCrossRef)

    /**
     * Inserta un logro y lo relaciona con un usuario en una transacción.
     *
     * @param achievement El logro a insertar.
     * @param userId El ID del usuario.
     */
    @Transaction
    fun insertAndRelate(achievement: Achievement, userId: Long) {
        // Inserta el logro
        insert(achievement)
        // Inserta la relación entre usuario y logro
        insertUserAchievement(UserAchievementCrossRef(userId, achievement.achievementId))
    }
}