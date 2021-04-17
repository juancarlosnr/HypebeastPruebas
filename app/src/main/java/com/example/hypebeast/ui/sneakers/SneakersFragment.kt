package com.example.hypebeast.ui.sneakers

import android.os.Bundle
import android.text.Layout
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hypebeast.R
import com.example.hypebeast.core.Result
import com.example.hypebeast.data.model.sneakers.sneakers
import com.example.hypebeast.data.remote.sneakers.SneakersDataSource
import com.example.hypebeast.databinding.FragmentSneakersBinding
import com.example.hypebeast.domain.sneakers.SneakersRepoImpl
import com.example.hypebeast.presentation.sneakers.SneakersViewModel
import com.example.hypebeast.presentation.sneakers.SneakersViewModelFactory
import com.example.hypebeast.ui.auth.RegisterFragment
import com.example.hypebeast.ui.details.DetailsFragment
import com.example.hypebeast.ui.sneakers.adapter.SneakersAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SneakersFragment : Fragment(R.layout.fragment_sneakers), SneakersAdapter.OnSneakerClickListener,SneakersAdapter.OnFavouritesClickListener, SneakersAdapter.OnRemoveFavouritesClickListener {

    private lateinit var binding: FragmentSneakersBinding
    private var gridLayoutManager: GridLayoutManager? = null
    private val viewModel by viewModels<SneakersViewModel> {
        SneakersViewModelFactory(SneakersRepoImpl(SneakersDataSource()))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSneakersBinding.bind(view)
        gridLayoutManager = GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)

        viewModel.fetchLatestSneakers().observe(viewLifecycleOwner, Observer { result ->
            when(result){
                is Result.Loading ->  {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Sucess -> {
                    binding.progressBar.visibility = View.GONE
                    binding.rvSneakers.layoutManager = gridLayoutManager
                    binding.rvSneakers.setHasFixedSize(true)
                    binding.rvSneakers.adapter = SneakersAdapter(result.data, this@SneakersFragment, this@SneakersFragment, this@SneakersFragment)
                }
                is Result.Failure ->{
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "Ocurrio un error ${result.exception}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun onSneakerClick(sneakers: sneakers) {
        val action = SneakersFragmentDirections.actionSneakersFragmentToDetailsFragment(
            sneakers.sneakers_picture,
            sneakers.sneakers_title,
            sneakers.sneakers_releasedate,
            sneakers.sneakers_description,
            sneakers.sneakers_releaseprice,
            sneakers.sneakers_resellprice,
            sneakers.sneakers_wherebuy
        )
            findNavController().navigate(action)
    }
    override fun onFavouritesClickListener(sneakers: sneakers) {

        CoroutineScope(Dispatchers.IO).launch {
        val uid = FirebaseAuth.getInstance().uid
        val db = FirebaseFirestore.getInstance().document("")
        db.collection("users").document("$uid").update("favorites_id", FieldValue.arrayUnion(sneakers.sneakers_id))
        }
        //Para eliminar se hace lo mismo que arriba lo unico que hay que indicarle es FildValue.arrayRemove
        //hacer metodo para que cuente clicks y una vez los haya contado y hagamos doble ckick hacer que elimine ese favorito.

    }

    override fun OnRemoveFavouritesClick(sneakers: sneakers) {

        CoroutineScope(Dispatchers.IO).launch {
           val uid = FirebaseAuth.getInstance().uid
            val db = FirebaseFirestore.getInstance()
           db.collection("users").document("$uid")
            db.collection("users").document("$uid").update("favorites_id", FieldValue.arrayRemove(sneakers.sneakers_id))
        }
    }
}