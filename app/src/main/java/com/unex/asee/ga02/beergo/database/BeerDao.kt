package com.unex.asee.ga02.beergo.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.unex.asee.ga02.beergo.model.Beer

@Dao
interface BeerDao {

    @Query("SELECT * FROM beer WHERE beerId = :id")
    suspend fun findById(id: Int): Beer
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(beer: Beer)
}