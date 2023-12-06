package com.unex.asee.ga02.beergo.repository

import com.unex.asee.ga02.beergo.database.AchievementDao
import com.unex.asee.ga02.beergo.database.UserDao

/**
 * Repositorio para manejar operaciones relacionadas con logros (Achievements).
 *
 * @property userDao DAO para la entidad de usuario.
 * @property achievementDao DAO para la entidad de logro (Achievement).
 */
class AchievementRepository private constructor(private val userDao: UserDao, private val achievementDao: AchievementDao) { //TODO: Sigue el patron singlenton implementado

    /**
     * Obtiene todos los logros.
     *
     * @return Lista de todos los logros.
     */
    suspend fun getAllAchievements() = achievementDao.getAll()

    /**
     * Obtiene los logros de un usuario específico.
     *
     * @param userId ID del usuario.
     * @return Logros del usuario.
     */
    suspend fun getUserAchievements(userId: Long) = userDao.getUserAchievements(userId)

    /**
     * Verifica si un usuario ha alcanzado un logro específico.
     *
     * @param userId ID del usuario.
     * @param achievementId ID del logro a verificar.
     * @return `true` si el usuario ha alcanzado el logro, `false` en caso contrario.
     */
    suspend fun checkAchievement(userId: Long, achievementId: Long): Boolean {
        val userAchievements = userDao.getUserAchievements(userId).achievements
        var exist = false
        userAchievements.forEach {
            if (it.achievementId == achievementId) {
                exist = true
            }
        }
        return exist
    }

    /**
     * Singleton para obtener una única instancia del repositorio.
     */
    companion object {
        @Volatile
        private var instance: AchievementRepository? = null

        /**
         * Obtiene o crea la instancia única del repositorio.
         *
         * @param userDao DAO para la entidad de usuario.
         * @param achievementDao DAO para la entidad de logro (Achievement).
         * @return Instancia única del repositorio.
         */
        fun getInstance(userDao: UserDao, achievementDao: AchievementDao) =
            instance ?: synchronized(this) {
                instance ?: AchievementRepository(userDao, achievementDao).also { instance = it }
            }
    }
}
