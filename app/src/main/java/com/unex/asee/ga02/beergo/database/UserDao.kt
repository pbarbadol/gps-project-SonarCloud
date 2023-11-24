package com.unex.asee.ga02.beergo.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.unex.asee.ga02.beergo.model.User

/**
 * Data Access Object (DAO) for User entity.
 */
@Dao
interface UserDao {

    /**
     * Busca un usuario por su nombre.
     *
     * @param first El nombre del usuario a buscar.
     * @return El usuario encontrado o null si no se encuentra.
     */
    @Query("SELECT * FROM user WHERE name LIKE :first LIMIT 1")
    suspend fun findByName(first: String): User?

    /**
     * Inserta un usuario en la base de datos.
     *
     * @param user El usuario a insertar.
     * @return El ID del usuario insertado.
     */
    @Insert
    suspend fun insert(user: User): Long

    /**
     * Elimina un usuario de la base de datos.
     *
     * @param user El usuario a eliminar.
     */
    @Delete
    suspend fun delete(user: User)

    /**
     * Obtiene el número de cervezas insertadas por un usuario específico.
     *
     * @param userId El ID del usuario.
     * @return El número de cervezas insertadas por el usuario.
     */
    @Query("SELECT COUNT(*) FROM Beer WHERE insertedBy = :userId")
    suspend fun countBeersInsertedByUser(userId: Long): Int

    /**
     * Obtiene el número de cervezas favoritas de un usuario específico.
     *
     * @param userId El ID del usuario.
     * @return El número de cervezas favoritas del usuario.
     */
    @Query("SELECT COUNT(*) FROM UserFavouriteBeerCrossRef WHERE userId = :userId")
    suspend fun countUserFavouriteBeers(userId: Long): Int

    /**
     * Obtiene el número de comentarios realizados por un usuario específico.
     *
     * @param userId El ID del usuario.
     * @return El número de comentarios realizados por el usuario.
     */
    @Query("SELECT COUNT(*) FROM Comment WHERE userId = :userId")
    suspend fun countCommentsByUser(userId: Long): Int

    /**
     * Obtiene el número de logros obtenidos por un usuario específico.
     *
     * @param userId El ID del usuario.
     * @return El número de logros obtenidos por el usuario.
     */
    @Query("SELECT COUNT(*) FROM UserAchievementCrossRef WHERE userId = :userId")
    suspend fun countUserAchievements(userId: Long): Int

}
