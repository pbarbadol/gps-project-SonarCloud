package com.unex.asee.ga02.beergo.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import androidx.room.ForeignKey

/**
 * Representa una cerveza en la aplicación BeerGo.
 *
 * @property beerId El ID único de la cerveza, generado automáticamente.
 * @property title El título de la cerveza.
 * @property description La descripción de la cerveza.
 * @property year El año de la cerveza.
 * @property abv El contenido de alcohol por volumen (ABV) de la cerveza.
 * @property image La ruta de la imagen de la cerveza.
 * @property insertedBy El ID del usuario que insertó la cerveza. Puede ser nulo si la cerveza no ha sido insertada por nadie.
 */
@Entity(
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["insertedBy"],
            onUpdate = ForeignKey.CASCADE, //Si cambiamos el ID de un usuario, se actualiza en la cerveza
            onDelete = ForeignKey.SET_NULL //Si borramos un usuario, se pone a null el ID de la cerveza
        )
    ]
)
data class Beer(
    @PrimaryKey(autoGenerate = true) val beerId: Long,
    val title: String,
    val description: String,
    val year: String,
    val abv: Double,
    val image: String,
    val insertedBy: Long? // El ID del usuario que insertó la cerveza. Puede ser nulo si no ha sido insertada por nadie.
) : Serializable



