package com.example.hypebeast.data.remote.profile

import android.util.Log
import com.example.hypebeast.data.model.profile.Favourites
import com.example.hypebeast.core.Result
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await as await

class FavouritesDataSource {

    suspend fun getFavouritesId(): Result<List<Favourites>> {
        val db = FirebaseFirestore.getInstance()
        val uid = FirebaseAuth.getInstance().uid
        val zapas: ArrayList<String> = arrayListOf()
        var todaszapas: DocumentSnapshot
        val favoritos: ArrayList<String> = arrayListOf()
        var documentos: ArrayList<String> = arrayListOf()
        val pruebadocumentos: ArrayList<String> = arrayListOf()
        val favouritesList = mutableListOf<Favourites>()
        val arraysfavoritos: ArrayList<String> = arrayListOf()



        val sneakers = db.collection("sneakers")
        val usuario = db.collection("users").document("$uid")
        val task1: Task<QuerySnapshot> = sneakers.get()
        val task2: Task<DocumentSnapshot> = usuario.get()

        Tasks.whenAllSuccess<Object>(task1, task2).addOnSuccessListener {
            val ids: QuerySnapshot = task1.getResult()!!
            val document: DocumentSnapshot = task2.getResult()!!
            Log.d("ids", ids.documents.size.toString())
            for(d in 0 until ids.documents.size){
                zapas.add(ids.documents[d].id)
                //Log.d("todaszapas2", "${ids.documents[d]}")
                todaszapas = ids.documents[d]

            }

            val documents = document.get("favorites_id")
            Log.d("pruebass", "$documents")
            documentos = document.get("favorites_id") as ArrayList<String>
            Log.d("favoritos", "$documentos")
            for (a in 0 until zapas.size){
                if (zapas[a] in documentos){
                    Log.d("sifavorito", zapas[a] + "si es favorito")
                    arraysfavoritos.add(zapas[a])

                    Log.d("arrayfavourties", "$arraysfavoritos")
                }
            }
            Log.d("pruebaarray", "$arraysfavoritos")
        }

        return Result.Sucess(favouritesList)
    }

}