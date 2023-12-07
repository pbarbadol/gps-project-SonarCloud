package com.unex.asee.ga02.beergo.repository

import com.unex.asee.ga02.beergo.database.CommentDao
import com.unex.asee.ga02.beergo.model.Comment

/**
 * Repositorio para gestionar la obtención y almacenamiento de comentarios relacionados con cervezas.
 *
 * Esta clase sigue el patrón Repository para abstraer el acceso a datos, proporcionando una interfaz
 * única para la lógica de la aplicación, independientemente de la fuente de datos subyacente.
 *
 * @property commentDao Instancia de CommentDao para acceder a la base de datos local.
 */
class CommentRepository private constructor(private val commentDao: CommentDao) { //TODO: Sigue el patron singlenton implementado

    // Variable para almacenar el tiempo de la última actualización de datos.
    private var lastUpdateTimeMillis: Long = 0L

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

    /**
     * Método para cargar comentarios de la base de datos local, intentando actualizar la caché si es necesario.
     *
     * @param beerId Identificador de la cerveza asociada a los comentarios.
     * @return Lista de objetos Comment obtenidos desde la base de datos local.
     */
    suspend fun loadComments(beerId: Long): List<Comment> {
        tryUpdateCommentsCache()
        return commentDao.findByBeer(beerId)
    }

    /**
     * Intenta actualizar la caché de comentarios si es necesario.
     */
    private suspend fun tryUpdateCommentsCache() {
        if (shouldUpdateCommentsCache()) {
            fetchComments() // Realiza la actualización de la caché.
        }
    }

    /**
     * Realiza la llamada a la API y actualiza la base de datos local con los datos obtenidos.
     *
     * Aquí implementarías la lógica para obtener los comentarios desde la API.
     * Después de obtener los comentarios, los insertas en la base de datos local.
     * Además, actualizas lastUpdateTimeMillis.
     */
    private suspend fun fetchComments() {
        // Lógica de obtención de comentarios desde la API y actualización de la base de datos local.
    }

    /**
     * Verifica si se debe actualizar la caché de comentarios.
     *
     * @return `true` si se debe actualizar, `false` de lo contrario.
     */
    private suspend fun shouldUpdateCommentsCache(): Boolean {
        val lastFetchTimeMillis = lastUpdateTimeMillis
        val timeFromLastFetch = System.currentTimeMillis() - lastFetchTimeMillis
        return timeFromLastFetch > MIN_TIME_FROM_LAST_FETCH_MILLIS
    }

    // Compañero del objeto Repository que gestiona la creación de instancias.
    companion object {
        // Tiempo mínimo (en milisegundos) que debe pasar antes de realizar una nueva actualización.
        private const val MIN_TIME_FROM_LAST_FETCH_MILLIS: Long = 30000

        // Instancia única del Repository utilizando el patrón Singleton.
        @Volatile
        private var INSTANCE: CommentRepository? = null

        /**
         * Obtiene la instancia única del Repository.
         *
         * @param commentDao Instancia de CommentDao para acceder a la base de datos local.
         * @return Instancia única del Repository.
         */
        fun getInstance(commentDao: CommentDao): CommentRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: CommentRepository(commentDao).also { INSTANCE = it }
            }
        }
    }
}