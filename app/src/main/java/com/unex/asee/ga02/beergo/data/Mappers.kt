package com.unex.asee.ga02.beergo.data

import com.unex.asee.ga02.beergo.data.api.BeerApi
import com.unex.asee.ga02.beergo.model.Beer

fun BeerApi.toBeer(): Beer {
    return Beer(
        beerId = (id ?: 0).toLong(),
        title = name ?: "",
        year = firstBrewed?.substring(3, 7) ?: "",
        description = description ?: "",
        abv = abv ?: 0.0,
        image = imageUrl ?: "",
        insertedBy = null
    )
}

fun Beer.toBeerApi(): BeerApi {
    return BeerApi(
        id = beerId,
        name = title,
        firstBrewed = year,
        description = description,
        abv = abv,
        imageUrl = image
    )
}