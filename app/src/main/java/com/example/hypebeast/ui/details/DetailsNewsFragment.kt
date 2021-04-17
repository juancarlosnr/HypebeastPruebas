package com.example.hypebeast.ui.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.hypebeast.R
import com.example.hypebeast.databinding.FragmentDetailsNewsBinding


class DetailsNewsFragment : Fragment(R.layout.fragment_details_news) {
    private lateinit var binding:FragmentDetailsNewsBinding
    private val args by navArgs<DetailsNewsFragmentArgs>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailsNewsBinding.bind(view)

        Glide.with(requireContext()).load("${args.newsPicture}").centerCrop().into(binding.imgNews)
        binding.txtTitle.text = args.newsTitle
        binding.txtDescription.text = args.newsDescription
        binding.txtDate.text = args.newsDate
        binding.txtWriter.text = args.newsAutor
    }
}