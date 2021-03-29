package com.example.hypebeast.data.remote.profile

import android.util.Log
import com.example.hypebeast.data.model.profile.Favourites
import com.example.hypebeast.core.Result
import com.example.hypebeast.data.model.sneakers.sneakers
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await as await
import kotlin.collections.emptyList as emptyList1

class FavouritesDataSource {

    fun getFavouritesId(): ArrayList<String> {
        val uid = FirebaseAuth.getInstance().uid
        val favouritesuser: ArrayList<String> = arrayListOf()
        val querySnapshot = FirebaseFirestore.getInstance().collection("users").document("$uid")
        querySnapshot.addSnapshotListener { snapshot, e ->

            if (e != null) {
                Log.d("FailureFavourites", "Listen failure")
            }
            if (snapshot != null && snapshot.exists()) {
                favouritesuser.add((snapshot.data?.get("favorites_id").toString()))
                Log.d("favoritos", "$favouritesuser")

            } else {
                Log.d("Fallo", "Current data:null")
            }

        }
        Log.d("pruebas2", "$favouritesuser")
        return favouritesuser
    }

        suspend fun getFavourites(): Result<List<Favourites>> {

            var favouriteuser: ArrayList<String> = getFavouritesId()
            Log.d("Prueba", "${favouriteuser[0]}")
            var favouritesList = mutableListOf<Favourites>()
            val querySnapshot = FirebaseFirestore.getInstance().collection("sneakers").get().await()


            for (favourite in querySnapshot.documents){
                if (favouriteuser == querySnapshot){
                favourite.toObject(Favourites::class.java)?.let { favoritos ->
                    favouritesList.add(favoritos)
                }
                }
            }
            return Result.Sucess(favouritesList)
        }
}