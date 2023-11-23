package com.unex.asee.ga02.beergo.data

import com.unex.asee.ga02.beergo.data.api.BeerApi
import com.unex.asee.ga02.beergo.model.Beer

fun BeerApi.toBeer(): Beer {
    return Beer(
        id = id ?: 0,
        title = name ?: "",
        year = firstBrewed?.substring(3, 7) ?: "",
        description = description ?: "",
        abv = abv ?: 0.0,
        image = imageUrl ?: ""
    )
}

fun Beer.toBeerApi(): BeerApi {
    return BeerApi(
        id = id,
        name = title,
        firstBrewed = year,
        description = description,
        abv = abv,
        imageUrl = image
    )
}