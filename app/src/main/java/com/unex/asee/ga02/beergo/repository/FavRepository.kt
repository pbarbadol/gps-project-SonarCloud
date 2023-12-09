package com.unex.asee.ga02.beergo.repository

import androidx.lifecycle.LiveData
import com.unex.asee.ga02.beergo.database.UserDao
import com.unex.asee.ga02.beergo.model.Beer
import com.unex.asee.ga02.beergo.model.UserFavouriteBeerCrossRef

class FavRepository (private val userDao: UserDao) {
    suspend fun addFav(userId: Long, beerId: Long) {
        userDao.insertAndRelateUserFavouriteBeer(userId, beerId)
    }
    fun loadFavs(userId: Long): LiveData<List<Beer>> {
        return userDao.getFavouritesBeersByUserId(userId)
    }
    suspend fun deleteFav(userId: Long, beerId: Long) {
        userDao.deleteUserFavouriteBeer(UserFavouriteBeerCrossRef(userId, beerId))
    }
}