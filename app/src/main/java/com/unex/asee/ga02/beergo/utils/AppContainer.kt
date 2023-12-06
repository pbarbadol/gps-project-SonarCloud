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
    val beerRepository = BeerRepository.getInstance(db!!.beerDao(), getNetworkService())
    val favRepository = FavRepository.getInstance(db!!.userDao())
    val achievmentRepository = AchievementRepository.getInstance(db!!.userDao(), db!!.achievementDao())
    val commentRepository = CommentRepository.getInstance(db!!.commentDao())
    val userRepository = UserRepository.getInstance(db!!.userDao())

}