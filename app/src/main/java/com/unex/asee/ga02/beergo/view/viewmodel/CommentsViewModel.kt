package com.unex.asee.ga02.beergo.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.unex.asee.ga02.beergo.BeerGoApplication
import com.unex.asee.ga02.beergo.model.Beer
import com.unex.asee.ga02.beergo.model.Comment
import com.unex.asee.ga02.beergo.model.User
import com.unex.asee.ga02.beergo.repository.BeerRepository
import com.unex.asee.ga02.beergo.repository.CommentRepository
import com.unex.asee.ga02.beergo.repository.UserRepository

class CommentsViewModel (
    private val beerRepository: BeerRepository,
    private val userRepository: UserRepository,
    private val commentRepository: CommentRepository
): ViewModel(){

    /**
     * Obtiene la cerveza seleccionada actualmente.
     * @return Método getSelectedBeer de beerRepository.
     */
    fun getSelectedBeer(): Beer? {
        return beerRepository.getSelectedBeer()
    }

    /**
     * Obtiene la el usuario logueado
     * @return Método getCurrentUser de userRepository.
     */
    fun getCurrentUser(): User? {
        return userRepository.getCurrentUser()
    }

    /**
     * Método para borrar un comentario de la base de datos local.
     *
     * @param comment Objeto Comment que se va a eliminar.
     * @return Método deleteComment de commentRepository.
     */
    suspend fun deleteComment(comment: Comment) {
        return commentRepository.deleteComment(comment)
    }

    /**
     * Método para cargar comentarios de la base de datos local, intentando actualizar la caché si es necesario.
     *
     * @param beerId Identificador de la cerveza asociada a los comentarios.
     * @return Método loadComments de commentRepository.
     */
    suspend fun loadComments(beerId: Long): List<Comment> {
        return commentRepository.loadComments(beerId)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T { // Get the Application object from extras

                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                return CommentsViewModel(
                    (application as BeerGoApplication).appContainer.beerRepository,
                    (application as BeerGoApplication).appContainer.userRepository,
                    (application as BeerGoApplication).appContainer.commentRepository,
                ) as T
            }
        }
    }
}