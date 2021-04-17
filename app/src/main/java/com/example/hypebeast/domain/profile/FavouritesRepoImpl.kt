package com.example.hypebeast.domain.profile

import com.example.hypebeast.core.Result
import com.example.hypebeast.data.model.profile.Favourites
import com.example.hypebeast.data.remote.profile.FavouritesDataSource

class FavouritesRepoImpl(private val dataSource: FavouritesDataSource): FavouritesRepo {
    override suspend fun getFavourites(): Result<List<Favourites>> = dataSource.getFavouritesId()


}