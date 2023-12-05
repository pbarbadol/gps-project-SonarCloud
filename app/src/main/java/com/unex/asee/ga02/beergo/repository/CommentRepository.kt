package com.unex.asee.ga02.beergo.repository
import com.unex.asee.ga02.beergo.database.CommentDao
import com.unex.asee.ga02.beergo.model.Comment
class CommentRepository private constructor(private val commentDao: CommentDao) {
    suspend fun addComment(comment: Comment) {
        commentDao.insert(comment)
    }
    suspend fun loadComments(beerId: Long): List<Comment> { //TODO: Seguramente haya que pasarlo mediante un viewModel
        return commentDao.findByBeer(beerId)
    }
    companion object {
        private var INSTANCE: CommentRepository? = null
        fun getInstance(commentDao: CommentDao): CommentRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: CommentRepository(commentDao).also { INSTANCE = it }
            }
        }
    }
}