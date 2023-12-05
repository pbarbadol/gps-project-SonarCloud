package com.unex.asee.ga02.beergo.repository

import com.unex.asee.ga02.beergo.database.BeerDao
import com.unex.asee.ga02.beergo.model.Beer
import com.unex.asee.ga02.beergo.model.UserFavouriteBeerCrossRef

class FavRepository private constructor(private val beerDao : BeerDao) {

//    suspend fun addFav(userId: Long, beerId: Long) {
//        beerDao.insertUserFavouriteBeer(userId, beerId)
//    }

    suspend fun addFav(userId: Long, beerId: Long) {
        val uFb = UserFavouriteBeerCrossRef(userId, beerId)
        //beerDao.insertUserFavouriteBeer(uFb) TODO: crear mas DAOS, uno para la tabla de cervezas y otro para la tabla de relaciones
    }

    suspend fun loadFavs(userId: Long): List<Beer> {
        return beerDao.getFavouritesBeersByUserId(userId)
    }
    suspend fun deleteFav(userId: Long, beerId: Long) {
        val uFb = UserFavouriteBeerCrossRef(userId, beerId)
        beerDao.deleteUserFavouriteBeer(uFb)
    }
    companion object {
        private var INSTANCE: FavRepository? = null
        fun getInstance(beerDao: BeerDao): FavRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: FavRepository(beerDao)
            }
        }
    }
}