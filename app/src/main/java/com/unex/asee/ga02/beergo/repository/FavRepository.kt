package com.unex.asee.ga02.beergo.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.unex.asee.ga02.beergo.database.UserDao
import com.unex.asee.ga02.beergo.model.Beer
import com.unex.asee.ga02.beergo.model.UserFavouriteBeerCrossRef

class FavRepository (private val userDao: UserDao) {
    private val userFilter = MutableLiveData<Long>()
    val favBeers: LiveData<List<Beer>> = userFilter.switchMap{ userId -> userDao.getFavouritesBeersByUserId(userId) }

    suspend fun addFav(userId: Long, beerId: Long) {
        userDao.insertAndRelateUserFavouriteBeer(userId, beerId)
    }
    fun setUserid(userid: Long) {
        userFilter.value = userid
    }
    /*
    fun loadFavs(userId: Long): LiveData<List<Beer>> {
        return userDao.getFavouritesBeersByUserId(userId)
    }*/
    suspend fun deleteFav(userId: Long, beerId: Long) {
        userDao.deleteUserFavouriteBeer(UserFavouriteBeerCrossRef(userId, beerId))
    }
}