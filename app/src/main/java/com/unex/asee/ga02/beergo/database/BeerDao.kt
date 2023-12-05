package com.unex.asee.ga02.beergo.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.unex.asee.ga02.beergo.model.Beer
import com.unex.asee.ga02.beergo.model.UserFavouriteBeerCrossRef

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
    fun getAll(): LiveData<List<Beer>> //TODO: refactor

    /**
     * Obtiene el numero de cervezas
     * @return Número de cervezas
     */
    @Query("SELECT COUNT(*) FROM beer")
    suspend fun getNumberBeers(): Long //TODO: refactor

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
    suspend fun insert(beer: Beer) //TODO: refactor

    /**
     * Inserta una lista de cervezas en la base de datos.
     *
     * @param beers La lista de cervezas a insertar.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(beers: List<Beer>) //TODO: refactor

    /**
     * Elimina una cerveza de la base de datos.
     *
     * @param beer La cerveza a eliminar.
     */
    @Delete
    suspend fun delete(beer: Beer)

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
     * Verifica si una cerveza está en favoritos.
     *
     * @param beerId El ID de la cerveza.
     * @return El número de ocurrencias en la tabla de favoritos (0 o 1).
     */
    @Query("SELECT COUNT(*) FROM userfavouritebeercrossref WHERE beerId = :beerId")
    suspend fun isBeerInFavorites(beerId: kotlin.Long): Int


    /**
     * Obtiene cuantas cervezas favoritas tiene un usuario.
     *
     * @param userId El ID del usuario.
     * @return Lista con las cervezas favoritas del usuario.
     */
    @Query("SELECT COUNT(*) FROM userfavouritebeercrossref WHERE userId = :userId ")
    suspend fun countBeerInFavorites(userId: Long): Int


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
     * @param userId El ID del usuario.
     * @param beerId El ID de la cerveza.
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
    suspend fun insertAndRelate(beer: Beer, userId: Long) {
        insertUserFavourite(UserFavouriteBeerCrossRef(userId, beer.beerId))
    }
}
