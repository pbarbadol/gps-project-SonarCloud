package com.unex.asee.ga02.beergo.utils

import android.content.Context
import com.unex.asee.ga02.beergo.api.getNetworkService
import com.unex.asee.ga02.beergo.database.BeerGoDatabase
import com.unex.asee.ga02.beergo.repository.AchievementRepository
import com.unex.asee.ga02.beergo.repository.BeerRepository
import com.unex.asee.ga02.beergo.repository.CommentRepository
import com.unex.asee.ga02.beergo.repository.FavRepository
import com.unex.asee.ga02.beergo.repository.UserRepository

class AppContainer(context: Context?) {
    val db = BeerGoDatabase.getInstance(context!!)
    private val networkService = getNetworkService()
    val beerRepository = BeerRepository(db!!.beerDao(), networkService)
    val favRepository = FavRepository(db!!.userDao())
    val achievmentRepository = AchievementRepository(db!!.userDao(), db.achievementDao())
    val commentRepository = CommentRepository(db!!.commentDao())
    val userRepository = UserRepository(db!!.userDao())

}