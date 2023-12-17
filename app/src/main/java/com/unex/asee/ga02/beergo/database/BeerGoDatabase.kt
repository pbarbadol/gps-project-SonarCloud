package com.unex.asee.ga02.beergo.database
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.unex.asee.ga02.beergo.data.beerAchievements
import com.unex.asee.ga02.beergo.data.dummyBeers
import com.unex.asee.ga02.beergo.model.Achievement
import com.unex.asee.ga02.beergo.model.Beer
import com.unex.asee.ga02.beergo.model.User
import com.unex.asee.ga02.beergo.model.UserFavouriteBeerCrossRef
import com.unex.asee.ga02.beergo.model.Comment
import com.unex.asee.ga02.beergo.model.UserAchievementCrossRef
import com.unex.asee.ga02.beergo.model.UserBeerCrossRef

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
                    ).addCallback(BeerGoCallback(context)).build()
                }
            }
            return INSTANCE
        }
        fun destroyInstance() {
            INSTANCE = null
        }
    }

    // Clase interna para realizar la precarga de datos
    private class BeerGoCallback(private val context: Context) : RoomDatabase.Callback()  {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            val achievementDao = getInstance(context)!!.achievementDao()
            val userDao = getInstance(context)!!.userDao()
            val beerDao = getInstance(context)!!.beerDao()
            val commentDao = getInstance(context)!!.commentDao()
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

                for (beer in dummyBeers) {
                    beerDao.insert(beer)
                }

                commentDao.insert(Comment(0, 1, 1, "¡Gran cerveza!", "admin"))
                commentDao.insert(Comment(0, 1, 2, "Me gusta el sabor.", "user"))
                commentDao.insert(Comment(0, 1, 3, "Recomiendo esta cerveza.", "user2"))
                commentDao.insert(Comment(0, 1, 4, "Buen aroma.", "user3"))
                commentDao.insert(Comment(0, 1, 5, "Sabor excepcional.", "user4"))
                commentDao.insert(Comment(0, 1, 6, "No está mal.", "Pablo"))
                commentDao.insert(Comment(0, 1, 7, "Buena elección.", "Alberto"))
                commentDao.insert(Comment(0, 1, 8, "Probé esta cerveza ayer.", "Gabriel"))
                commentDao.insert(Comment(0, 1, 9, "¡Increíble!", "Garrido"))
                commentDao.insert(Comment(0, 1, 10, "Muy sabrosa.", "RobertoRE"))
                commentDao.insert(Comment(0, 1, 11, "Me gusta mucho.", "JavierBerr"))

                commentDao.insert(Comment(0, 2, 10, "Esta cerveza es increíble.", "RobertoRE"))
                commentDao.insert(Comment(0, 2, 11, "No estoy de acuerdo, no me gusta tanto.", "JavierBerr"))
                commentDao.insert(Comment(0, 2, 10, "¿En serio? ¿Has probado todas las cervezas?", "RobertoRE"))
                commentDao.insert(Comment(0, 2, 11, "No necesito probarlas todas para tener una opinión.", "JavierBerr"))
                commentDao.insert(Comment(0, 2, 10, "Pero esta tiene un sabor único.", "RobertoRE"))
                commentDao.insert(Comment(0, 2, 11, "No sé, no me convence.", "JavierBerr"))
                commentDao.insert(Comment(0, 2, 10, "Bueno, cada uno tiene sus gustos.", "RobertoRE"))
                commentDao.insert(Comment(0, 2, 11, "Exacto, respetemos nuestras opiniones.", "JavierBerr"))
                commentDao.insert(Comment(0, 2, 10, "¡De acuerdo, hagámoslo!", "RobertoRE"))
                commentDao.insert(Comment(0, 2, 11, "Salud.", "JavierBerr"))

                commentDao.insert(Comment(0, 3, 6, "¡Pablo, esta cerveza es impresionante!", "RobertoRE"))
                commentDao.insert(Comment(0, 3, 6, "Por cierto, me has caído muy bien en la asignatura. Voy a ponerte un 10.", "RobertoRE"))
                commentDao.insert(Comment(0, 3, 6, "¡En serio? ¡Gracias Roberto! No esperaba eso.", "Pablo"))
                commentDao.insert(Comment(0, 3, 6, "Sí, has demostrado un gran conocimiento en la materia. ¡Enhorabuena!", "RobertoRE"))
            }
        }
    }
}



