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
}
