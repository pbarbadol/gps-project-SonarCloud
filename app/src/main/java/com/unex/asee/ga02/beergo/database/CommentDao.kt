package com.unex.asee.ga02.beergo.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.unex.asee.ga02.beergo.model.Comment

/**
 * Data Access Object (DAO) for Comment entity.
 */
@Dao
interface CommentDao {

    /**
     * Encuentra un comentario por su ID.
     *
     * @param id El ID del comentario a buscar.
     * @return El comentario encontrado.
     */
    @Query("SELECT * FROM comment WHERE commentId = :id")
    suspend fun findById(id: Long): Comment? //En ocasiones, puede ser que el comentario no se encuentre. Por ello, usamos el operador "?" para indicar un retorno null

    /**
     * Inserta un comentario en la base de datos.
     *
     * @param comment El comentario a insertar.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(comment: Comment)

    /**
     * Elimina un comentario de la base de datos.
     *
     * @param comment El comentario a eliminar.
     */
    @Delete
    suspend fun delete(comment: Comment)

    /**
     * Encuentra comentarios por el ID de la cerveza.
     *
     * @param beerId El ID de la cerveza.
     * @return Una lista de comentarios para la cerveza especificada.
     */
    @Query("SELECT * FROM comment WHERE beerId = :beerId")
    fun findByBeer(beerId: Long): LiveData<List<Comment>>
}
