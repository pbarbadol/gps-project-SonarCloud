package com.unex.asee.ga02.beergo.data

import com.unex.asee.ga02.beergo.model.Beer
import com.unex.asee.ga02.beergo.model.Comment
import com.unex.asee.ga02.beergo.model.User

val dummyBeers: List<Beer> = listOf(
    Beer(
        0,
        "Heineken",
        "Heineken es una cerveza tipo Lager, fundada en 1873 en Ámsterdam. Es conocida por su sabor suave y su carácter refrescante.",
        "1873",
        42.0,
        "R.drawable.hnk", // Cambia esto por la ruta correcta de tu imagen
        null // No se especifica el usuario que la insertó
    ),
    Beer(
        1,
        "Corona",
        "Corona es una cerveza de tipo pale lager que se originó en México en 1925. Es reconocida por su sabor ligero y su característica rodaja de limón.",
        "1925",
        38.0,
        "R.drawable.corona", // Cambia esto por la ruta correcta de tu imagen
        null // No se especifica el usuario que la insertó
    ),
    Beer(
        2,
        "Guinness",
        "Guinness es una cerveza negra tipo stout, con sede en Dublín, Irlanda. Es famosa por su sabor a malta tostada y su textura cremosa.",
        "1759",
        125.0,
        "R.drawable.guinness", // Cambia esto por la ruta correcta de tu imagen
        null // No se especifica el usuario que la insertó
    ),
    Beer(
        3,
        "Stella Artois",
        "Stella Artois es una cerveza tipo pilsner originaria de Bélgica. Conocida por su sabor distintivo y su aroma floral.",
        "1366",
        40.0,
        "R.drawable.stellaartois", // Cambia esto por la ruta correcta de tu imagen
        null // No se especifica el usuario que la insertó
    ),
    Beer(
        4,
        "Budweiser",
        "Budweiser es una cerveza tipo lager estadounidense fundada en 1876. Se destaca por su sabor suave y su popularidad a nivel internacional.",
        "1876",
        45.0,
        "R.drawable.budweiser", // Cambia esto por la ruta correcta de tu imagen
        null // No se especifica el usuario que la insertó
    ),
    Beer(
        5,
        "Estrella Galicia",
        "Estrella Galicia es una cerveza tipo Lager, producida en Galicia, España. Es conocida por su sabor equilibrado y su arraigo a la tradición gallega.",
        "1906",
        38.0,
        "R.drawable.estrellagalicia", // Cambia esto por la ruta correcta de tu imagen
        null // No se especifica el usuario que la insertó
    ),
    Beer(
        6,
        "Amstel lata",
        "Amstel es una cerveza tipo pilsner originaria de los Países Bajos. Es conocida por su sabor fresco y su suavidad.",
        "1870",
        40.0,
        "R.drawable.amstel_lata", // Cambia esto por la ruta correcta de tu imagen
        null // No se especifica el usuario que la insertó
    )
)

val dummyUsers : List<User> = listOf(
    User(0, "admin", "admin"),
    User(0, "user", "user"),
    User(0, "user2", "user2"),
    User(0, "user3", "user3"),
    User(0, "user4", "user4"),
    User(0, "Pablo", "1234"),
    User(0, "Alberto", "1234"),
    User(0, "Gabriel", "1234"),
    User(0, "Garrido", "1234"),
    User(0, "RobertoRE", "1234"),
    User(0, "JavierBerr", "1234")
)

val dummyComments: List<Comment> = listOf(
    Comment(0, 1, 1, "¡Gran cerveza!", "admin"),
    Comment(0, 1, 2, "Me gusta el sabor.", "user"),
    Comment(0, 1, 3, "Recomiendo esta cerveza.", "user2"),
    Comment(0, 1, 4, "Buen aroma.", "user3"),
    Comment(0, 1, 5, "Sabor excepcional.", "user4"),
    Comment(0, 1, 6, "No está mal.", "Pablo"),
    Comment(0, 1, 7, "Buena elección.", "Alberto"),
    Comment(0, 1, 8, "Probé esta cerveza ayer.", "Gabriel"),
    Comment(0, 1, 9, "¡Increíble!", "Garrido"),
    Comment(0, 1, 10, "Muy sabrosa.", "RobertoRE"),
    Comment(0, 1, 11, "Me gusta mucho.", "JavierBerr"),

    Comment(0, 2, 10, "Esta cerveza es increíble.", "RobertoRE"),
    Comment(0, 2, 11, "No estoy de acuerdo, no me gusta tanto.", "JavierBerr"),
    Comment(0, 2, 10, "¿En serio? ¿Has probado todas las cervezas?", "RobertoRE"),
    Comment(0, 2, 11, "No necesito probarlas todas para tener una opinión.", "JavierBerr"),
    Comment(0, 2, 10, "Pero esta tiene un sabor único.", "RobertoRE"),
    Comment(0, 2, 11, "No sé, no me convence.", "JavierBerr"),
    Comment(0, 2, 10, "Bueno, cada uno tiene sus gustos.", "RobertoRE"),
    Comment(0, 2, 11, "Exacto, respetemos nuestras opiniones.", "JavierBerr"),
    Comment(0, 2, 10, "¡De acuerdo, hagámoslo!", "RobertoRE"),
    Comment(0, 2, 11, "Salud.", "JavierBerr"),

    Comment(0, 3, 6, "¡Pablo, esta cerveza es impresionante!", "RobertoRE"),
    Comment(0, 3, 6, "Por cierto, me has caído muy bien en la asignatura. Voy a ponerte un 10.", "RobertoRE"),
    Comment(0, 3, 6, "¡En serio? ¡Gracias Roberto! No esperaba eso.", "Pablo"),
    Comment(0, 3, 6, "Sí, has demostrado un gran conocimiento en la materia. ¡Enhorabuena!", "RobertoRE")
)

