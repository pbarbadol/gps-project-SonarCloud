package com.unex.asee.ga02.beergo.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.unex.asee.ga02.beergo.BeerGoApplication
import com.unex.asee.ga02.beergo.model.Beer
import com.unex.asee.ga02.beergo.model.Comment
import com.unex.asee.ga02.beergo.model.User
import com.unex.asee.ga02.beergo.repository.BeerRepository
import com.unex.asee.ga02.beergo.repository.CommentRepository
import kotlinx.coroutines.launch

class AddCommentViewModel(
    private val commentRepository: CommentRepository

) : ViewModel() {
    var user: User? = null
    var beer: Beer? = null


    /**
     * Escribe el comentario a partir del parametro de entrada, el nombre de usuario, el id de usuario y de la cerveza.
     * @param contenido Contenido del comentario.
     * @return Booleano que indica si se ha escrito el comentario o no.
     */
    fun writeComment(contenido: String): Boolean {
        return if (contenido.isEmpty()) {
            false
        } else {
            val comment = Comment(
                commentId = 0,
                beerId = beer!!.beerId,
                userId = user!!.userId,
                comment = contenido,
                userName = user!!.name
            )
            viewModelScope.launch {
                commentRepository.addComment(comment)
            }
            true
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>, extras: CreationExtras
            ): T { // Get the Application object from extras

                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                return AddCommentViewModel(
                    (application as BeerGoApplication).appContainer.commentRepository,
                ) as T
            }
        }
    }
}