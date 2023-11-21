package com.unex.asee.ga02.beergo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.unex.asee.ga02.beergo.model.Beer
import com.unex.asee.ga02.beergo.model.User
import com.unex.asee.ga02.beergo.model.Comment

@Database(entities = [User::class, Comment::class, Beer::class], version = 1)
abstract class BeerGoDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao

    abstract fun commentDao(): CommentDao

    abstract fun beerDao(): BeerDao

    companion object {
        private var INSTANCE: BeerGoDatabase? = null

        fun getInstance(context: Context): BeerGoDatabase? {
            if (INSTANCE == null) {
                synchronized(BeerGoDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context,
                        BeerGoDatabase::class.java,
                        "beergo.db"
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
