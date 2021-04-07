package com.example.hypebeast.ui.profile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hypebeast.core.BaseViewHolder
import com.example.hypebeast.data.model.profile.Favourites
import com.example.hypebeast.databinding.FavouritesItemViewBinding
import com.example.hypebeast.ui.sneakers.adapter.SneakersAdapter

class FavouritesAdapter(private val favouriteslist: List<Favourites>):
    RecyclerView.Adapter<BaseViewHolder<*>>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val itemBinding = FavouritesItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val holder = FavouritesFragmentViewHolder(itemBinding, parent.context)


        return holder
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when(holder){
            is FavouritesFragmentViewHolder -> holder.bind(favouriteslist[position])
        }
    }

   override fun getItemCount(): Int = favouriteslist.size

    private inner class FavouritesFragmentViewHolder(
        val binding: FavouritesItemViewBinding,
        val context: Context
    ): BaseViewHolder<Favourites>(binding.root) {
        override fun bind(item: Favourites) {
            Glide.with(context).load(item.sneakers_picture).centerCrop().into(binding.cardviewImgview)
            binding.cardviewTitle.text = item.sneakers_title
        }
    }
}