package com.example.hypebeast.ui.home.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.hypebeast.ui.home.HomeFragment
import com.example.hypebeast.ui.home.SegundoFragment
import com.example.hypebeast.ui.home.TercerFragment

class ViewPagerHomeAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
       return  when(position){
            0 ->{
                HomeFragment()
            }
            1 ->{
                SegundoFragment()
            }
            2 ->{
                TercerFragment()
            }
            else ->{
                Fragment()
            }
        }

    }
}