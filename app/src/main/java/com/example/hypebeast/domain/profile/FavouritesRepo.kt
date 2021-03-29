package com.example.hypebeast.domain.profile

import com.example.hypebeast.data.model.profile.Favourites
import com.example.hypebeast.core.Result

interface FavouritesRepo {
    suspend fun getFavourites():Result<List<Favourites>>
}