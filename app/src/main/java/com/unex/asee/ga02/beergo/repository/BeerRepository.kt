package com.unex.asee.ga02.beergo.repository

import com.unex.asee.ga02.beergo.api.BeerApiInterface
import com.unex.asee.ga02.beergo.api.getNetworkService
import com.unex.asee.ga02.beergo.data.toBeer
import com.unex.asee.ga02.beergo.database.BeerDao
import com.unex.asee.ga02.beergo.model.Beer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repositorio para gestionar la obtención y almacenamiento de datos relacionados con cervezas.
 *
 * Esta clase sigue el patrón Repository para abstraer el acceso a datos, proporcionando una interfaz
 * única para la lógica de la aplicación, independientemente de la fuente de datos subyacente.
 *
 * @property beerDao Instancia de BeerDao para acceder a la base de datos local.
 */
class BeerRepository
private constructor(private val beerDao: BeerDao) {
    // Variable para almacenar el tiempo de la última actualización de datos.
    private var lastUpdateTimeMillis: Long = 0L
    // Lista de cervezas obtenidas de la base de datos local.
    val beers = beerDao.getAll()

    /**
     *  Añade una cerveza a la base de datos local.
     */
    suspend fun addBeer(beer: Beer) {
        beerDao.insert(beer)
    }

    /**
     * Intenta actualizar la caché de cervezas recientes si es necesario.
     */
    suspend fun tryUpdateRecentBeersCache() {
        if (shouldUpdateBeersCache()) fetchBeers()
    }

    /**
     * Realiza una llamada a la API para obtener la lista de cervezas desde la red.
     *
     * @return Lista de objetos Beer obtenidos desde la API.
     */
    private suspend fun fetchBeersFromApi(): List<Beer> = withContext(Dispatchers.IO) {
        try {
            val result = getNetworkService().getBeers(1).execute()

            if (result.isSuccessful) {
                result.body()?.map { it?.toBeer() ?: Beer(0, "", " ", " ", 0.0, "", 0) }
                    ?: throw Exception("Response body is null")
            } else {
                throw Exception("Error: ${result.code()} ${result.message()}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    /**
     * Realiza la llamada a la API y actualiza la base de datos local con los datos obtenidos.
     */
    private suspend fun fetchBeers()
    {
        val beers = fetchBeersFromApi()
        beerDao.insertAll(beers)
    }

    /**
     * Verifica si se debe actualizar la caché de cervezas.
     *
     * @return `true` si se debe actualizar, `false` de lo contrario.
     */
    private suspend fun shouldUpdateBeersCache(): Boolean {
        val lastFetchTimeMillis = lastUpdateTimeMillis
        val timeFromLastFetch = System.currentTimeMillis() - lastFetchTimeMillis
        return timeFromLastFetch > MIN_TIME_FROM_LAST_FETCH_MILLIS || beerDao.getNumberBeers() == 0L
    }

    // Compañero del objeto Repository que gestiona la creación de instancias.
    companion object {
        // Tiempo mínimo (en milisegundos) que debe pasar antes de realizar una nueva actualización.
        private const val MIN_TIME_FROM_LAST_FETCH_MILLIS: Long = 3000000

        // Instancia única del Repository utilizando el patrón Singleton.
        @Volatile
        private var INSTANCE: BeerRepository? = null

        /**
         * Obtiene la instancia única del Repository.
         *
         * @param beerDao Instancia de BeerDao para acceder a la base de datos local.
         * @param beerAPI Instancia de BeerApiInterface para realizar llamadas a la API.
         * @return Instancia única del Repository.
         */
        fun getInstance(beerDao: BeerDao, beerAPI: BeerApiInterface): BeerRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: BeerRepository(
                    beerDao
                ).also { INSTANCE = it }
            }
        }
    }
}