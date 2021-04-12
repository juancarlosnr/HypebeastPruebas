package com.example.hypebeast.ui.profile

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapFactory.*
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.hypebeast.core.Result
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hypebeast.R
import com.example.hypebeast.data.model.profile.Favourites
import com.example.hypebeast.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.example.hypebeast.data.remote.profile.FavouritesDataSource
import com.example.hypebeast.domain.profile.FavouritesRepoImpl
import com.example.hypebeast.presentation.profile.FavouritesModelFactory
import com.example.hypebeast.presentation.profile.FavouritesViewModel
import com.example.hypebeast.ui.profile.adapter.FavouritesAdapter
import com.example.hypebeast.ui.sneakers.SneakersFragmentDirections
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.firestore.FieldValue

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File


class ProfileFragment : Fragment(R.layout.fragment_profile), FavouritesAdapter.OnFavouriteClickListener {

    private var gridLayoutManager: GridLayoutManager? = null
    private lateinit var binding: FragmentProfileBinding
    private val viewModel by viewModels<FavouritesViewModel> { FavouritesModelFactory(FavouritesRepoImpl(
        FavouritesDataSource()
    ))}


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)
        gridLayoutManager = GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)

        binding.fbtakephoto.setOnClickListener {
            ImagePicker.with(this)
                .galleryOnly()
                .compress(1024)         //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    1080,
                    1080
                )  //Final image resolution will be less than 1080 x 1080(Optional)
                .start { resultCode, data ->
                    if (resultCode == Activity.RESULT_OK) {
                        //Image Uri will not be null for RESULT_OK
                        val fileUri = data?.data
                        binding.circleImageView.setImageURI(fileUri)

                        //You can get File object from intent
                        val fichero: File? = ImagePicker.getFile(data)
                        Log.d("datos", "$fichero")

                    } else if (resultCode == ImagePicker.RESULT_ERROR) {
                        Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        viewModel.getFavourites().observe(viewLifecycleOwner, Observer { result ->
            when(result){
                is Result.Loading ->{
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Sucess ->{
                    binding.progressBar.visibility = View.GONE
                    binding.rvFavourites.layoutManager = gridLayoutManager
                    binding.rvFavourites.setHasFixedSize(true)
                    binding.rvFavourites.adapter = FavouritesAdapter(result.data, this@ProfileFragment)
                }
                is Result.Failure  ->{
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "Ocurrio un error ${result.exception}", Toast.LENGTH_SHORT).show()
                }
            }
        })

        getUserName()
        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_settingsFragment)
        }
        binding.textView2.setOnClickListener {
            val customDialog = AlertDialog.Builder(context)
                .setTitle("Cerrar sesión")
                .setMessage("¿Estas seguro de que deseas cerrar sesión?")
                .setNegativeButton("No"){ view, _->
                    view.dismiss()
                }
                .setPositiveButton("Si"){ view, _->
                    FirebaseAuth.getInstance().signOut()
                    findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
                    view.dismiss()
                }
                .setCancelable(false)
                .create()

            customDialog.show()
           }
        }

    fun getUserName() {

        CoroutineScope(Dispatchers.IO).launch {

        val uid = FirebaseAuth.getInstance().uid
        val querySnapshot = FirebaseFirestore.getInstance().collection("users").document("$uid")
        querySnapshot.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.d("FailureFavourites", "Listen failure")
            }
            if (snapshot != null && snapshot.exists()) {
                val username = snapshot.data?.get("username")
                binding.textView.text = username.toString()
            } else {
                Log.d("Fallo", "Current data:null")
            }
            }
         }
    }

    override fun onFavouriteClick(Favourites: Favourites) {
        val action = ProfileFragmentDirections.actionProfileFragmentToDetailsFragment(
            Favourites.sneakers_picture,
            Favourites.sneakers_title,
            Favourites.sneakers_releasedate,
            Favourites.sneakers_description,
            Favourites.sneakers_releaseprice,
            Favourites.sneakers_resellprice
        )
        findNavController().navigate(action)
    }
}