package com.example.hypebeast.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hypebeast.R
import com.example.hypebeast.databinding.FragmentViewPagerHomeBinding
import com.example.hypebeast.ui.home.adapter.ViewPagerHomeAdapter
import com.example.hypebeast.ui.onBoarding.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import androidx.fragment.app.FragmentManager as FragmentManager1


class ViewPagerHome : Fragment(R.layout.fragment_view_pager_home) {
    private lateinit var binding:FragmentViewPagerHomeBinding
    private lateinit var viewPagerHomeAdapter:ViewPagerHomeAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentViewPagerHomeBinding.bind(view)

        viewPagerHomeAdapter = ViewPagerHomeAdapter(this)
        binding.viewPagerNews.adapter = viewPagerHomeAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPagerNews){tab, position ->
            when(position){
                0->{
                    tab.text="Novedades"
                }
                1->{
                    tab.text="Looks"
                }
                2->{
                    tab.text ="Proximamente"
                }
            }
        }.attach()
    }
}