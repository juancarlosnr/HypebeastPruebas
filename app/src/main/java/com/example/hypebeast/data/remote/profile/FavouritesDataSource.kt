package com.example.hypebeast.data.remote.profile

import android.util.Log
import com.example.hypebeast.data.model.profile.Favourites
import com.example.hypebeast.core.Result
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.ArrayList
import kotlinx.coroutines.tasks.await as await

class FavouritesDataSource {

    suspend fun getFavouritesId(): Result<List<Favourites>> {
        val uid = FirebaseAuth.getInstance().uid
        var favouritesList = mutableListOf<Favourites>()
        val fireStore = FirebaseFirestore.getInstance()
        val ref = fireStore.collection("users").document("$uid")
        val sneakers = fireStore.collection("sneakers").whereEqualTo("sneakers_id", true)
        Log.d("idsneakers", "$sneakers")
        val sneakers2 = FirebaseFirestore.getInstance().collection("sneakers").get().await()

        sneakers.get().addOnSuccessListener { ids ->
            for( sneaker in ids){
                Log.d("Hola", "${sneaker.data}")
            }
        }
            .addOnFailureListener { exception ->
                Log.d("Fallo", exception.toString())
            }

        ref.get().addOnSuccessListener { document ->
            val documentos: ArrayList<String> = document.get("favorites_id") as ArrayList<String>
            for (d in documentos) {
                Log.d("prueba2", "$d")
                if (d == sneakers) {
                    for (favourite in sneakers2.documents) {
                        favourite.toObject(Favourites::class.java)?.let { favoritos ->
                            favouritesList.add(favoritos)
                            Log.d("prueba", "$favouritesList")
                        }
                    }
                }

            }
        }
        return Result.Sucess(favouritesList)
    }
}