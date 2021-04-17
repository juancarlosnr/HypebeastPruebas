package com.example.hypebeast.ui.sneakers.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hypebeast.R
import com.example.hypebeast.core.BaseViewHolder
import com.example.hypebeast.data.model.sneakers.sneakers
import com.example.hypebeast.databinding.SneakersItemViewBinding

class SneakersAdapter(private val sneakerslist: List<sneakers>, private val itemClickListener: OnSneakerClickListener, private val bottomClickListener: OnFavouritesClickListener, private val removeBottomClickListener: OnRemoveFavouritesClickListener):RecyclerView.Adapter<BaseViewHolder<*>>(){

    interface OnSneakerClickListener{
        fun onSneakerClick(sneakers: sneakers)
    }
    interface OnFavouritesClickListener{
        fun onFavouritesClickListener(sneakers: sneakers)
    }
    interface OnRemoveFavouritesClickListener{
        fun OnRemoveFavouritesClick(sneakers: sneakers)
    }


    @SuppressLint("ResourceAsColor")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val itemBinding = SneakersItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val holder = SneakersFragmentViewHolder(itemBinding, parent.context)

        itemBinding.root.setOnClickListener{
            val position = holder.adapterPosition.takeIf { it != DiffUtil.DiffResult.NO_POSITION }
                ?: return@setOnClickListener
            itemClickListener.onSneakerClick(sneakerslist[position])
        }
        itemBinding.btn.setOnClickListener { view ->
            val position = holder.adapterPosition.takeIf { it != DiffUtil.DiffResult.NO_POSITION }

                ?: return@setOnClickListener
            bottomClickListener.onFavouritesClickListener(sneakerslist[position])
            itemBinding.btnRemoveFav.visibility = View.VISIBLE
            itemBinding.btnRemoveFav.isEnabled = true
        }
        itemBinding.btnRemoveFav.setOnClickListener {
            val position = holder.adapterPosition.takeIf { it != DiffUtil.DiffResult.NO_POSITION }
                ?:return@setOnClickListener
            removeBottomClickListener.OnRemoveFavouritesClick(sneakerslist[position])
            itemBinding.btnRemoveFav.visibility = View.INVISIBLE
            itemBinding.btnRemoveFav.isEnabled = false
        }

        return holder
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when(holder){
            is SneakersFragmentViewHolder -> holder.bind(sneakerslist[position])
        }
    }

    override fun getItemCount(): Int = sneakerslist.size


    private inner class SneakersFragmentViewHolder(
        val binding:SneakersItemViewBinding,
        val context: Context):BaseViewHolder<sneakers>(binding.root) {
        override fun bind(item: sneakers) {
            Glide.with(context).load(item.sneakers_picture).centerCrop().into(binding.cardviewImgview)
            binding.cardviewTitle.text = item.sneakers_title
        }
    }
}