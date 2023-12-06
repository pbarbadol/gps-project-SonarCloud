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

class AddCommentViewModel(
    private val beerRepository: BeerRepository,
    private val userRepository: UserRepository,
    private val commentRepository: CommentRepository
): ViewModel() {

    /**
     * Obtiene la el usuario logueado
     * @return Método getCurrentUser de userRepository
     */
    fun getCurrentUser(): User? {
        return userRepository.getCurrentUser()
    }

    /**
     * Obtiene la cerveza seleccionada actualmente.
     * @return Método getSelectedBeer de beerRepository
     */
    fun getSelectedBeer(): Beer? {
        return beerRepository.getSelectedBeer()
    }

    /**
     * Método para agregar un comentario a la base de datos local.
     * @return Método addComment de commentRepository
     */
    suspend fun addComment(comment: Comment){
        return commentRepository.addComment(comment)
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
                return AddCommentViewModel(
                    (application as BeerGoApplication).appContainer.beerRepository,
                    (application as BeerGoApplication).appContainer.userRepository,
                    (application as BeerGoApplication).appContainer.commentRepository,
                ) as T
            }
        }
    }





}