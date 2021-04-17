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
        val favouritesList = mutableListOf<Favourites>()
        val fireStore = FirebaseFirestore.getInstance()
        val ref = fireStore.collection("users").document("$uid")
        var documentos: ArrayList<String> = arrayListOf()
        val pruebaDocuments: ArrayList<String> = arrayListOf()
        val zapas: ArrayList<String> = arrayListOf()
        val favoritos: ArrayList<String> = arrayListOf()
        val sneakers = fireStore.collection("sneakers")
        sneakers.get().addOnSuccessListener { ids ->
            for (d in 0 until ids.documents.size) {
                Log.d("idsSneaker1: ", ids.documents[d].id)
                zapas.add(ids.documents[d].id)
            }
        }

        ref.get().addOnSuccessListener { document ->
            documentos = document.get("favorites_id") as ArrayList<String>
            Log.d("documentos", "$documentos")
            for (d in documentos) {
                Log.d("prueba2", "$d")
                pruebaDocuments.add(d)
            }
        }
        Log.d("documents", "$documentos")
        for (d in 0 until zapas.size) {
            Log.d("ZAPAAAS", "${zapas[d]}")
            if (zapas.isEmpty()) {
                println("VACIO ZAPAS")
            }
        }
        for (d in 0 until pruebaDocuments.size) {
            Log.d("DOCUMENTS:", "${pruebaDocuments[d]}")
            if (pruebaDocuments.isEmpty()) {
                println("VACIO DOCUMENTS")
            }
        }
        for (d in zapas.indices) {
            println(d)
            for (a in pruebaDocuments.indices) {
                println(a)
                if (zapas[d] == pruebaDocuments[a]) {
                    favoritos.add(zapas[d])
                }
            }
        }
        for (d in favoritos.indices) {
            Log.d("favoritos", "${favoritos[d]}")
        }
        //Esto es una prueba
        val querySnapshot = FirebaseFirestore.getInstance().collection("sneakers").get().await()
        for (sneaker in querySnapshot.documents) {
            sneaker.toObject(Favourites::class.java)?.let { Favourites ->
                favouritesList.add(Favourites)
            }
        }

        return Result.Sucess(favouritesList)
    }

}