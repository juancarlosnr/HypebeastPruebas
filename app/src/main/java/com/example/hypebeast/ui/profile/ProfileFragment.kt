package com.example.hypebeast.ui.profile

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
import com.example.hypebeast.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.example.hypebeast.data.remote.profile.FavouritesDataSource
import com.example.hypebeast.domain.profile.FavouritesRepoImpl
import com.example.hypebeast.presentation.profile.FavouritesModelFactory
import com.example.hypebeast.presentation.profile.FavouritesViewModel
import com.example.hypebeast.ui.profile.adapter.FavouritesAdapter

import com.google.firebase.firestore.FirebaseFirestore


class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var gridLayoutManager: GridLayoutManager? = null
    private lateinit var binding: FragmentProfileBinding
    private val viewModel by viewModels<FavouritesViewModel> { FavouritesModelFactory(FavouritesRepoImpl(
        FavouritesDataSource()
    ))}


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)
        gridLayoutManager = GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)


        viewModel.getFavourites().observe(viewLifecycleOwner, Observer { result ->
            when(result){
                is Result.Loading ->{
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Sucess ->{
                    binding.progressBar.visibility = View.GONE
                    binding.rvFavourites.layoutManager = gridLayoutManager
                    binding.rvFavourites.setHasFixedSize(true)
                    binding.rvFavourites.adapter = FavouritesAdapter(result.data)
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
            FirebaseAuth.getInstance().signOut()
            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
        }
    }

    fun getUserName() {
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