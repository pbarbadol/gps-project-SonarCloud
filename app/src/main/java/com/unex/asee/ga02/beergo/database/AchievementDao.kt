package com.unex.asee.ga02.beergo.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.unex.asee.ga02.beergo.model.Achievement
import com.unex.asee.ga02.beergo.model.UserAchievementCrossRef
import com.unex.asee.ga02.beergo.model.UserWithAchievements

//================= Interfaz del DAO para la entidad Achievement =================
@Dao
interface AchievementDao {

    //================= Consulta para obtener todos los logros =================
    @Query("SELECT * FROM Achievement")
    suspend fun getAll(): List<Achievement>

    //================= Consulta para obtener un logro por su ID =================
    @Query("SELECT * FROM Achievement WHERE AchievementId = :id")
    suspend fun findById(id: Int): Achievement

    //================= Inserta o reemplaza un logro =================
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(achievement: Achievement)

    //================= Elimina un logro =================
    @Delete
    suspend fun delete(achievement: Achievement)

    //================= Consulta para obtener un usuario con sus logros =================
    @Transaction
    @Query("SELECT * FROM User where userId = :userId")
    suspend fun getUserWithAchievements(userId: Long): UserWithAchievements

    //================= Inserta una relación entre usuario y logro =================
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUserAchievement(crossRef: UserAchievementCrossRef)

    //================= Transacción para insertar un logro y relacionarlo con un usuario =================
    @Transaction
    suspend fun insertAndRelate(achievement: Achievement, userId: Long) {
        // Inserta el logro
        insert(achievement)
        // Inserta la relación entre usuario y logro
        insertUserAchievement(UserAchievementCrossRef(userId, achievement.achievementId))
    }
}
