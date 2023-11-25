package com.unex.asee.ga02.beergo.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.unex.asee.ga02.beergo.model.Beer
import com.unex.asee.ga02.beergo.model.UserFavouriteBeerCrossRef
import com.unex.asee.ga02.beergo.model.UserWithFavourites

/**
 * Data Access Object (DAO) for Beer entity.
 */
@Dao
interface BeerDao {

    /**
     * Obtiene todas las cervezas.
     *
     * @return Lista con todas las cervezas.
     */
    @Query("SELECT * FROM beer")
    suspend fun getAll(): List<Beer>

    /**
     * Encuentra una cerveza por su ID.
     *
     * @param id El ID de la cerveza a buscar.
     * @return La cerveza encontrada.
     */
    @Query("SELECT * FROM beer WHERE beerId = :id")
    suspend fun findById(id: Int): Beer

    /**
     * Inserta una cerveza en la base de datos.
     *
     * @param beer La cerveza a insertar.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(beer: Beer)

    /**
     * Elimina una cerveza de la base de datos.
     *
     * @param beer La cerveza a eliminar.
     */
    @Delete
    suspend fun delete(beer: Beer)

    /**
     * Obtiene un usuario con sus cervezas favoritas.
     *
     * @param userId El ID del usuario.
     * @return [UserWithFavourites] que contiene el usuario y sus cervezas favoritas.
     */
    @Transaction
    @Query("SELECT * FROM user WHERE userId = :userId")
    suspend fun getUserWithFavourites(userId: Long): UserWithFavourites


    /**
     * Verifica si una cerveza está en favoritos.
     *
     * @param beerId El ID de la cerveza.
     * @return El número de ocurrencias en la tabla de favoritos (0 o 1).
     */
    @Query("SELECT COUNT(*) FROM userfavouritebeercrossref WHERE beerId = :beerId")
    suspend fun isBeerInFavorites(beerId: Int): Int


    /**
     * Obtiene todas las cervezas favoritas de un usuario.
     *
     * @param userId El ID del usuario.
     * @return Lista con las cervezas favoritas del usuario.
     */
    @Query("SELECT COUNT(*) FROM userfavouritebeercrossref WHERE userId = :userId ")
    suspend fun BeerInFavorites(userId: Long): Int


    @Query("SELECT COUNT(*) FROM comment WHERE userId = :userId ")
    suspend fun getNumberComments(userId: Long): Int

    /**
     * Obtiene todas las cervezas insertadas por los usuarios
     *
     * @param beerId El ID de la cerveza.
     * @return Lista con las cervezas de los usuarios.
     */
    @Query("SELECT * FROM beer WHERE insertedBy IS NOT NULL ")
    suspend fun getBeersFromAllUser(): List<Beer>

    /**
     * Obtiene el número de cervezas insertadas por los usuarios
     *
     * @param beerId El ID de la cerveza.
     * @return Número de cervezas de los usuarios.
     */
    @Query("SELECT COUNT(*) FROM beer WHERE insertedBy IS NOT NULL ")
    suspend fun getNumberBeersFromAllUser(): Int
    /**
     * Inserta una relación de usuario y cerveza favorita.
     *
     * @param crossRef La relación a insertar.
     */

    /**
     * Obtiene todas las cervezas insertadas por un usuario específico.
     *
     * @param userId El ID del usuario.
     * @return Lista con las cervezas del usuario.
     */
    @Query("SELECT * FROM beer WHERE insertedBy = :userId")
    suspend fun getBeersByUserId(userId: Long): List<Beer>


    /**
     * Obtiene la cantidad de cervezas insertadas por un usuario específico.
     *
     * @param userId El ID del usuario.
     * @return Cantidad de cervezas del usuario.
     */
    @Query("SELECT COUNT(*) FROM beer WHERE insertedBy = :userId")
    suspend fun getBeerCountByUserId(userId: Long): Int


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUserFavourite(crossRef: UserFavouriteBeerCrossRef)

    /**
     * Elimina una relación de usuario y cerveza favorita.
     *
     * @param crossRef La relación a eliminar.
     */
    @Delete
    suspend fun deleteUserFavourite(crossRef: UserFavouriteBeerCrossRef)

    /**
     * Elimina una cerveza de los favoritos de un usuario.
     *
     * @param beer La cerveza a eliminar.
     * @param userId El ID del usuario.
     */
    @Transaction
    suspend fun deleteAndRelate(beer: Beer, userId: Long) {
        deleteUserFavourite(UserFavouriteBeerCrossRef(userId, beer.beerId))
    }

    /**
     * Inserta una cerveza en los favoritos de un usuario.
     *
     * @param beer La cerveza a insertar.
     * @param userId El ID del usuario.
     */
    @Transaction
    suspend fun insertAndRelate(beer: Beer, userId: Long) {
        insertUserFavourite(UserFavouriteBeerCrossRef(userId, beer.beerId))
    }
}
