package com.unex.asee.ga02.beergo.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.unex.asee.ga02.beergo.model.Beer
import com.unex.asee.ga02.beergo.model.User
import com.unex.asee.ga02.beergo.model.UserFavouriteBeerCrossRef
import com.unex.asee.ga02.beergo.model.UserWithAchievements

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
     * Busca un usuario por su identificador.
     *
     * @param userId El identificador del usuario a buscar.
     * @return El usuario encontrado o null si no se encuentra.
     */
    @Query("SELECT * FROM user WHERE userId = :userId")
    suspend fun findByID(userId: Long): User?

    /**
     * Inserta un usuario en la base de datos.
     *
     * @param user El usuario a insertar.
     * @return El ID del usuario insertado.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User): Long

    /**
     * Elimina un usuario de la base de datos.
     *
     * @param user El usuario a eliminar.
     */
    @Delete
    suspend fun delete(user: User)

    /**
     * Obtiene todas las cervezas insertadas por un usuario específico.
     *
     * @param userId El ID del usuario.
     * @return Lista con las cervezas del usuario.
     */
    @Query("SELECT * FROM beer WHERE insertedBy = :userId")
    suspend fun getBeersByUserId(userId: Long): List<Beer>

    /**
     * Obtiene el número de cervezas insertadas por un usuario específico.
     *
     * @param userId El ID del usuario.
     * @return El número de cervezas insertadas por el usuario.
     */
    @Query("SELECT COUNT(*) FROM Beer WHERE insertedBy = :userId")
    suspend fun countBeersInsertedByUser(userId: Long): Int

    /**
     * Obtiene todas las cervezas favoritas de un usuario.
     *
     * @param userId El ID del usuario.
     * @return Lista con las cervezas favoritas del usuario.
     */
    @Transaction
    @Query("SELECT * FROM Beer WHERE beerId IN (SELECT beerId FROM userfavouritebeercrossref WHERE userId = :userId)")
    suspend fun getFavouritesBeersByUserId(userId: Long): List<Beer>

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
     * Obtiene un usuario con sus logros.
     *
     * @param userId El ID del usuario.
     * @return [UserWithAchievements] que contiene el usuario y sus logros.
     */
    @Transaction
    @Query("SELECT * FROM User where userId = :userId")
    suspend fun getUserAchievements(userId: Long): UserWithAchievements

    /**
     * Obtiene el número de logros obtenidos por un usuario específico.
     *
     * @param userId El ID del usuario.
     * @return El número de logros obtenidos por el usuario.
     */
    @Query("SELECT COUNT(*) FROM UserAchievementCrossRef WHERE userId = :userId")
    suspend fun countUserAchievements(userId: Long): Int

    /**
     * Obtiene el número de comentarios realizados por un usuario específico.
     *
     * @param userId El ID del usuario.
     * @return El número de comentarios realizados por el usuario.
     */
    @Query("SELECT COUNT(*) FROM comment WHERE userId = :userId ")
    suspend fun getNumberComments(userId: Long): Int

    /**
     * Comprueba si un usuario tiene un logro.
     *
     * @param userId El ID del usuario.
     * @param achievementId El ID del logro.
     * @return El número de logros encontrados.
     */
    @Query("SELECT COUNT(*) FROM UserAchievementCrossRef WHERE userId = :userId AND achievementId = :achievementId")
    suspend fun checkAchievement(userId: Long, achievementId: Long): Int

    /**
     * Inserta una relación de usuario y cerveza favorita.
     *
     * @param crossRef La relación de usuario y cerveza favorita a insertar.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUserFavouriteBeer(crossRef: UserFavouriteBeerCrossRef)

    /**
     * Elimina una relación de usuario y cerveza favorita.
     *
     * @param crossRef La relación de usuario y cerveza favorita a eliminar.
     */
    @Delete
    suspend fun deleteUserFavouriteBeer(crossRef: UserFavouriteBeerCrossRef)

    /**
     * Inserta una cerveza en los favoritos de un usuario.
     *
     * @param beer La cerveza a insertar.
     * @param userId El ID del usuario.
     */
    @Transaction
    suspend fun insertAndRelateUserFavouriteBeer(userId: Long, beerId: Long) {
        insertUserFavouriteBeer(UserFavouriteBeerCrossRef(userId, beerId))
    }

}
