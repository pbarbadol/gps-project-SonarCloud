package com.unex.asee.ga02.beergo.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.unex.asee.ga02.beergo.model.Beer

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
    fun getAll(): LiveData<List<Beer>>

    /**
     * Obtiene el numero de cervezas
     * @return Número de cervezas
     */
    @Query("SELECT COUNT(*) FROM beer")
    suspend fun getNumberBeers(): Long

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
     * Inserta una lista de cervezas en la base de datos.
     *
     * @param beers La lista de cervezas a insertar.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(beers: List<Beer>)

    /**
     * Elimina una cerveza de la base de datos.
     *
     * @param beer La cerveza a eliminar.
     */
    @Delete
    suspend fun delete(beer: Beer)

    /**
     * Obtiene el número de cervezas insertadas por los usuarios
     *
     * @param beerId El ID de la cerveza.
     * @return Número de cervezas de los usuarios.
     */
    @Query("SELECT COUNT(*) FROM beer WHERE insertedBy IS NOT NULL ")
    suspend fun getNumberBeersFromAllUser(): Int


}
