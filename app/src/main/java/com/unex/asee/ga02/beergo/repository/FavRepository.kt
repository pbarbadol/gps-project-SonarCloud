package com.unex.asee.ga02.beergo.repository

import com.unex.asee.ga02.beergo.database.BeerDao
import com.unex.asee.ga02.beergo.database.UserDao
import com.unex.asee.ga02.beergo.model.Beer
import com.unex.asee.ga02.beergo.model.UserFavouriteBeerCrossRef

class FavRepository private constructor(private val userDao: UserDao) {
    suspend fun addFav(userId: Long, beerId: Long) {
        userDao.insertAndRelateUserFavouriteBeer(beerId, userId)
    }
    suspend fun loadFavs(userId: Long): List<Beer> {
        return userDao.getFavouritesBeersByUserId(userId)
    }
    suspend fun deleteFav(userId: Long, beerId: Long) {
        val uFb = UserFavouriteBeerCrossRef(userId, beerId)
        userDao.deleteUserFavouriteBeer(uFb)
    }
    suspend fun isFavorite(userId: Long, beerId: Long): Boolean {
        val beers = userDao.getFavouritesBeersByUserId(userId)
        var isFav = false
        beers.forEach {
            if (it.beerId == beerId) {
                isFav = true
            }
        }
        return isFav
    }
    companion object {
        private var INSTANCE: FavRepository? = null
        fun getInstance(userDao: UserDao): FavRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: FavRepository(userDao)
            }
        }
    }
}