import com.unex.asee.ga02.beergo.model.Beer
import java.io.Serializable
import java.util.Date

data class History(

    var beer: Beer,
    var date: Date
) : Serializable {

    companion object {
        // Lista para almacenar las instancias de History
        private val historyList = mutableListOf<History>()

        // Función para guardar una instancia de History en la lista
        fun saveHistory(history: History) {
            historyList.add(history)
        }

        // Función para obtener la lista de historias
        fun getHistoryList(): List<History> {
            return historyList.toList()
        }
    }
}
