import com.unex.asee.ga02.beergo.model.Beer
import java.io.Serializable
import java.util.Date

/**
 * La clase `History` representa un registro de la historia de consumo de cervezas en la aplicación BeerGo.
 *
 * @property beer La cerveza asociada al registro de historia.
 * @property date La fecha en que se registró el consumo.
 */
data class History(
    var beer: Beer,
    var date: Date
) : Serializable {

    companion object {
        // Lista para almacenar las instancias de `History`.
        private val historyList = mutableListOf<History>()

        /**
         * Guarda una instancia de `History` en la lista de historias.
         *
         * @param history La instancia de `History` a guardar.
         */
        fun saveHistory(history: History) {
            historyList.add(history)
        }

        /**
         * Obtiene la lista de historias almacenadas.
         *
         * @return Lista de instancias de `History`.
         */
        fun getHistoryList(): List<History> {
            return historyList.toList()
        }
    }
}
