package com.unex.asee.ga02.beergo.view.achievement

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.unex.asee.ga02.beergo.R
import com.unex.asee.ga02.beergo.databinding.FragmentListAchievementBinding
import com.unex.asee.ga02.beergo.model.Achievement
import com.unex.asee.ga02.beergo.model.Comment

/**
 * Adaptador para la lista de logros.
 *
 * @property achievements Lista de logros a mostrar.
 * @property userAchievements Lista de logros que tiene el usuario.
 * @property onClick Función de clic corto.
 * @property onLongClick Función de clic largo.
 */
class AchievementsAdapter(
    private var achievements: List<Achievement>,
    private var userAchievements: List<Achievement>,
    private val onClick: (Achievement) -> Unit,
    private val onLongClick: (Achievement) -> Unit
) : RecyclerView.Adapter<AchievementsAdapter.ShowAchievementHolder>() {

    /**
     * Clase interna que representa un ViewHolder para los elementos de la lista.
     *
     * @property binding Referencia al objeto de enlace de datos.
     */
    inner class ShowAchievementHolder(private val binding: FragmentListAchievementBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Método para enlazar los datos del logro con la vista.
         *
         * @param achievement Logro a mostrar.
         */
        fun bind(achievement: Achievement) {
            with(binding) {

                // Verificar si el logro está en la lista de userAchievements
                val isAchievementUnlocked = userAchievements.contains(achievement)

                // Establecer la imagen del barril según si el logro está desbloqueado o no, y si está desbloqueado, según la categoria
                imgConseguido.setImageResource(
                    if (isAchievementUnlocked) {
                        when (achievement.category) {
                            "Barril de madera" -> R.drawable.barril
                            "Barril de plata" -> R.drawable.barril_plata
                            "Barril de oro" -> R.drawable.barril_oro
                            "Barril de diamante" -> R.drawable.barril_diamante
                            else -> R.drawable.barril
                        }
                    } else {
                        R.drawable.barril_vacio
                    }
                )

                // Establecer el título del logro
                nameAchievement.text = if (isAchievementUnlocked) achievement.title else "Logro Desconocido"

                // Establecer la categoría del logro
                typeAchievement.text = achievement.category

            }
        }

        /**
         * Inicialización del ViewHolder.
         */
        init {
            // Configuración del clic corto
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val achievement = achievements[position]
                    onClick.invoke(achievement)
                }
            }

            // Configuración del clic largo
            binding.root.setOnLongClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val achievement = achievements[position]
                    onLongClick.invoke(achievement)
                    return@setOnLongClickListener true
                }
                return@setOnLongClickListener false
            }
        }
    }

    fun updateData(achievement:  List<Achievement>) {
        achievements = achievement
        notifyDataSetChanged()
    }
    fun updateDataUser(achievement:  List<Achievement>) {
        userAchievements = achievement
        notifyDataSetChanged()
    }


    /**
     * Crea un nuevo ViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowAchievementHolder {
        // Infla el diseño de elemento de lista y lo pasa al constructor del ViewHolder
        val binding = FragmentListAchievementBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ShowAchievementHolder(binding)
    }

    /**
     * Obtiene la cantidad total de elementos en la lista.
     */
    override fun getItemCount(): Int {
        return achievements.size
    }

    /**
     * Vincula los datos de un elemento en la posición dada con un ViewHolder.
     */
    override fun onBindViewHolder(holder: ShowAchievementHolder, position: Int) {
        holder.bind(achievements[position])
    }
}
