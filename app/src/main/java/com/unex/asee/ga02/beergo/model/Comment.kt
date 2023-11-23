package com.unex.asee.ga02.beergo.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * La entidad Comment representa un comentario realizado por un usuario sobre una cerveza en la aplicación BeerGo.
 * Cada comentario tiene un identificador único autoincremental (commentId), una referencia a la cerveza (beerId),
 * el usuario que lo hizo (userId) y el propio comentario (comment).
 */
@Entity(
    foreignKeys = [
        // Clave foránea que vincula la columna "userId" de Comment con la columna "userId" de la entidad User.
        ForeignKey(
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        // Clave foránea que vincula la columna "beerId" de Comment con la columna "beerId" de la entidad Beer.
        ForeignKey(
            entity = Beer::class,
            parentColumns = ["beerId"],
            childColumns = ["beerId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Comment(
    // Identificador único autoincremental del comentario.
    @PrimaryKey(autoGenerate = true) var commentId: Long?,
    // Referencia a la cerveza asociada al comentario.
    val beerId: Int,
    // Usuario que realizó el comentario.
    val userId: Long?,
    // El comentario en sí.
    val comment: String,
    // El nombre del usuario que pone el comentario
    val userName: String
): Serializable