package com.unex.asee.ga02.beergo.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

class CommentsViewModel(
    private val beerRepository: BeerRepository, private val commentRepository: CommentRepository
) : ViewModel() {

    var user: User? = null

    val beerComment = commentRepository.commentBeers
    var beer: Beer? = null
        set(value) {
            field = value
            commentRepository.setBeerId(value!!.beerId)
        }

    private val _toast = MutableLiveData<String?>()
    val toast: LiveData<String?>
        get() = _toast

    /**
     * Método para borrar un comentario
     *
     * @param comment Objeto Comment que se va a eliminar.
     * @return True si se elimina, False en caso contrario.
     */
    fun deleteComment(comment: Comment): Boolean {
        if (comment.userId == user!!.userId) {
            viewModelScope.launch {
                commentRepository.deleteComment(comment)
            }
            _toast.value = "Comentario eliminado correctamente"
            return true
        } else _toast.value = "No puedes eliminar este comentario sinverguenza!"
        return false
    }

    /**
     * Método para cargar comentarios de la base de datos local, intentando actualizar la caché si es necesario.
     *
     * @param beerId Identificador de la cerveza asociada a los comentarios.
     * @return Método loadComments de commentRepository.
     */
    fun onToastShown() {
        _toast.value = null
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>, extras: CreationExtras
            ): T { // Get the Application object from extras

                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                return CommentsViewModel(
                    (application as BeerGoApplication).appContainer.beerRepository,
                    (application as BeerGoApplication).appContainer.commentRepository,
                ) as T
            }
        }
    }
}