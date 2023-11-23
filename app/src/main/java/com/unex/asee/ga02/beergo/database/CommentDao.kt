package com.unex.asee.ga02.beergo.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.unex.asee.ga02.beergo.model.Comment

@Dao
interface CommentDao {

    @Query("SELECT * FROM comment WHERE commentId = :id")
    suspend fun findById(id: Long): Comment

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(comment: Comment)

    @Delete
    suspend fun delete(comment: Comment)

    @Query("SELECT * FROM comment WHERE beerId = :beerId")
    suspend fun findByBeer(beerId: Int): List<Comment>
}