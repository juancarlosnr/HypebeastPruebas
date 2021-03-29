package com.example.hypebeast.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.hypebeast.domain.profile.FavouritesRepo
import kotlinx.coroutines.Dispatchers
import com.example.hypebeast.core.Result
import java.lang.Exception

class FavouritesViewModel(private val repo: FavouritesRepo):ViewModel() {
    fun getFavourites() = liveData(Dispatchers.IO) {
        emit(Result.Loading())
        try {
            emit(repo.getFavourites())
        }catch (e:Exception){
            emit(Result.Failure(e))
        }
    }
}
class FavouritesModelFactory(private val repo: FavouritesRepo): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(FavouritesRepo::class.java).newInstance(repo)
    }
}