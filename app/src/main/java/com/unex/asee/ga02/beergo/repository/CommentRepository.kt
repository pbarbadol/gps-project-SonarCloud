package com.unex.asee.ga02.beergo.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.unex.asee.ga02.beergo.database.CommentDao
import com.unex.asee.ga02.beergo.model.Beer
import com.unex.asee.ga02.beergo.model.Comment

/**
 * Repositorio para gestionar la obtención y almacenamiento de comentarios relacionados con cervezas.
 *
 * Esta clase sigue el patrón Repository para abstraer el acceso a datos, proporcionando una interfaz
 * única para la lógica de la aplicación, independientemente de la fuente de datos subyacente.
 *
 * @property commentDao Instancia de CommentDao para acceder a la base de datos local.
 */
class CommentRepository (private val commentDao: CommentDao) {

    private val beerFilter = MutableLiveData<Long>()
    val commentBeers: LiveData<List<Comment>> = beerFilter.switchMap{ beerId -> commentDao.findByBeer(beerId) }
    fun setBeerId(beerId: Long) {
        beerFilter.value = beerId
    }

    /**
     * Método para agregar un comentario a la base de datos local.
     *
     * @param comment Objeto Comment que se va a agregar.
     */
    suspend fun addComment(comment: Comment) {
        commentDao.insert(comment)
    }

    /**
     * Método para borrar un comentario de la base de datos local.
     *
     * @param comment Objeto Comment que se va a eliminar.
     */
    suspend fun deleteComment(comment: Comment) {
        commentDao.delete(comment)
    }

    // Compañero del objeto Repository que gestiona la creación de instancias.
    companion object {
        // Tiempo mínimo (en milisegundos) que debe pasar antes de realizar una nueva actualización.
        private const val MIN_TIME_FROM_LAST_FETCH_MILLIS: Long = 30000
    }
}