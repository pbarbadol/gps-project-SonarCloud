package com.unex.asee.ga02.beergo.database
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.unex.asee.ga02.beergo.data.beerAchievements
import com.unex.asee.ga02.beergo.model.Achievement
import com.unex.asee.ga02.beergo.model.Beer
import com.unex.asee.ga02.beergo.model.User
import com.unex.asee.ga02.beergo.model.UserFavouriteBeerCrossRef
import com.unex.asee.ga02.beergo.model.Comment
import com.unex.asee.ga02.beergo.model.UserAchievementCrossRef
import com.unex.asee.ga02.beergo.model.UserBeerCrossRef
import com.unex.asee.ga02.beergo.utils.ChallengeAchievementFunction.DatabaseObserver
import com.unex.asee.ga02.beergo.utils.ChallengeAchievementFunction.DatabaseSubject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Room Database para la aplicación BeerGo.
 * Contiene entidades: User, Beer, Comment, UserFavouriteBeerCrossRef, Achievement, UserAchievementCrossRef.
 */
@Database(
    entities = [User::class, Beer::class, Comment::class, UserFavouriteBeerCrossRef::class, Achievement::class, UserAchievementCrossRef::class, UserBeerCrossRef::class],
    version = 2
) //La base de datos se encuentra en versión 2 debido a que se ha hecho una actualización de los atributos de la clase Beer

abstract class BeerGoDatabase : RoomDatabase(), DatabaseSubject {
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
                    ).addCallback(BeerGoCallback(context)).build()
                }
            }
            return INSTANCE
        }
        fun destroyInstance() {
            INSTANCE = null
        }
    }
    private val databaseObservers = mutableMapOf<String, MutableList<DatabaseObserver>>()
    override fun addDatabaseObserver(tableName: String, observer: DatabaseObserver) {
        val observers = databaseObservers.getOrPut(tableName) { mutableListOf() }
        observers.add(observer)
    }
    override fun removeDatabaseObserver(tableName: String, observer: DatabaseObserver) {
        val observers = databaseObservers[tableName]
        observers?.remove(observer)
    }
    override fun notifyDatabaseObservers(tableName: String) {
        val observers = databaseObservers[tableName]
        observers?.forEach { observer -> observer.onTableChanged(tableName) }
    }
    // Clase interna para realizar la precarga de datos
    private class BeerGoCallback(private val context: Context) : RoomDatabase.Callback()  {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            val achievementDao = getInstance(context)!!.achievementDao()
            val userDao = getInstance(context)!!.userDao()
            //Inserción de los logros en la base de datos
            GlobalScope.launch(Dispatchers.IO) {
                for (achievement in beerAchievements) {
                    achievementDao.insert(achievement)
                }
                userDao.insert(User(0, "admin", "admin"))
                userDao.insert(User(0, "user", "user"))
                userDao.insert(User(0, "user2", "user2"))
                userDao.insert(User(0, "user3", "user3"))
                userDao.insert(User(0, "user4", "user4"))
                userDao.insert(User(0, "Pablo", "1234"))
                userDao.insert(User(0, "Alberto", "1234"))
                userDao.insert(User(0, "Gabriel", "1234"))
                userDao.insert(User(0, "Garrido", "1234"))
                userDao.insert(User(0, "RobertoRE", "1234"))
                userDao.insert(User(0, "JavierBerr", "1234"))

            }
        }
    }
}



