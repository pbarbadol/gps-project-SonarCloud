package com.unex.asee.ga02.beergo.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.unex.asee.ga02.beergo.model.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user WHERE name LIKE :first LIMIT 1")
    suspend fun findByName(first: String): User?
    @Insert
    suspend fun insert(user: User): Long

    @Delete
    fun delete(user : User)

}