package com.unex.asee.ga02.beergo.repository

import com.unex.asee.ga02.beergo.database.AchievementDao
import com.unex.asee.ga02.beergo.database.UserDao

/**
 * Repositorio para manejar operaciones relacionadas con logros (Achievements).
 *
 * @property userDao DAO para la entidad de usuario.
 * @property achievementDao DAO para la entidad de logro (Achievement).
 */
class AchievementRepository (private val userDao: UserDao, private val achievementDao: AchievementDao) {

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
     fun getUserAchievements(userId: Long) = userDao.getUserAchievements(userId).value?.achievements

    /**
     * Verifica si un usuario ha alcanzado un logro específico.
     *
     * @param userId ID del usuario.
     * @param achievementId ID del logro a verificar.
     * @return `true` si el usuario ha alcanzado el logro, `false` en caso contrario.
     */
//    suspend fun checkAchievement(userId: Long, achievementId: Long): Boolean {
//        val userAchievements = userDao.getUserAchievements(userId).achievements
//        var exist = false
//        userAchievements.forEach {
//            if (it.achievementId == achievementId) {
//                exist = true
//            }
//        }
//        return exist
//    }
}
