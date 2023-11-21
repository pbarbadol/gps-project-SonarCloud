package com.unex.asee.ga02.beergo.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.unex.asee.ga02.beergo.model.Beer
import com.unex.asee.ga02.beergo.model.UserFavouriteBeerCrossRef
import com.unex.asee.ga02.beergo.model.UserWithFavourites
@Dao
interface BeerDao {

    @Query("SELECT * FROM beer WHERE beerId = :id")
    suspend fun findById(id: Int): Beer

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(beer: Beer)

    @Delete
    suspend fun delete(beer: Beer)

    @Transaction
    @Query("SELECT * FROM user WHERE userId = :userId")
    suspend fun getUserWithFavourites(userId: Long): UserWithFavourites

    @Query("SELECT COUNT(*) FROM userfavouritebeercrossref WHERE beerId = :beerId")
    suspend fun isBeerInFavorites(beerId: Int): Int

    @Insert (onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUserFavourite(crossRef: UserFavouriteBeerCrossRef)

    @Delete
    suspend fun deleteUserFavourite(crossRef: UserFavouriteBeerCrossRef)

    @Transaction
    suspend fun deleteAndRelate(beer: Beer, userId: Long){
        deleteUserFavourite(UserFavouriteBeerCrossRef(userId, beer.beerId))
    }

    @Transaction
    suspend fun insertAndRelate(beer: Beer, userId: Long){
        insertUserFavourite(UserFavouriteBeerCrossRef(userId, beer.beerId))
    }
}