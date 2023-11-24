package com.unex.asee.ga02.beergo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.unex.asee.ga02.beergo.model.Achievement
import com.unex.asee.ga02.beergo.model.Beer
import com.unex.asee.ga02.beergo.model.User
import com.unex.asee.ga02.beergo.model.UserFavouriteBeerCrossRef
import com.unex.asee.ga02.beergo.model.Comment
import com.unex.asee.ga02.beergo.model.UserAchievementCrossRef

/**
 * Room Database para la aplicación BeerGo.
 * Contiene entidades: User, Beer, Comment, UserFavouriteBeerCrossRef, Achievement, UserAchievementCrossRef.
 */
@Database(
    entities = [User::class, Beer::class, Comment::class, UserFavouriteBeerCrossRef::class, Achievement::class, UserAchievementCrossRef::class],
    version = 2
) //La base de datos se encuentra en versión 2 debido a que se ha hecho una actualización de los atributos de la clase Beer

abstract class BeerGoDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun achievementDao(): AchievementDao

    abstract fun beerDao(): BeerDao

    abstract fun commentDao(): CommentDao

    companion object {
        private var INSTANCE: BeerGoDatabase? = null

        fun getInstance(context: Context): BeerGoDatabase? {
            if (INSTANCE == null) {
                synchronized(BeerGoDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context, BeerGoDatabase::class.java, "beergo.db"
                    ).build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}
