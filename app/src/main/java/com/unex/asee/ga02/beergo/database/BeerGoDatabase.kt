package com.unex.asee.ga02.beergo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.unex.asee.ga02.beergo.model.Beer
import com.unex.asee.ga02.beergo.model.User
import com.unex.asee.ga02.beergo.model.UserFavouriteBeerCrossRef

//La base de datos se encuentra en versión 2 debido a que se ha hecho una actualización de los atributos de la clase Beer
@Database(entities = [User::class, Beer::class, UserFavouriteBeerCrossRef::class], version = 2)
abstract class BeerGoDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao

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
                    )
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}
