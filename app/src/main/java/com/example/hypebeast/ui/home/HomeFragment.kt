package com.example.hypebeast.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.hypebeast.R
import com.example.hypebeast.core.Result
import com.example.hypebeast.data.model.home.news
import com.example.hypebeast.data.remote.home.HomeDataSource
import com.example.hypebeast.databinding.FragmentHomeBinding
import com.example.hypebeast.domain.home.HomeRepoImpl
import com.example.hypebeast.presentation.HomeViewModel
import com.example.hypebeast.presentation.HomeViewModelFactory
import com.example.hypebeast.ui.home.adapter.HomeAdapter


class HomeFragment : Fragment(R.layout.fragment_home), HomeAdapter.OnNewsClickListener {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModels<HomeViewModel> {
        HomeViewModelFactory(
            HomeRepoImpl(
                HomeDataSource()
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        viewModel.fecthLatestNews().observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Sucess -> {
                    binding.progressBar.visibility = View.GONE
                    binding.rvHome.adapter = HomeAdapter(result.data, this@HomeFragment)
                }
                is Result.Failure ->{
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "Ocurrio un error ${result.exception}", Toast.LENGTH_SHORT).show()
                }
            }
        })


    }

    override fun onNewsClick(news: news) {
        val action = HomeFragmentDirections.actionHomeFragmentToDetailsNewsFragment(
            news.news_picture,
            news.news_title,
            news.news_description,
            news.news_date,
            news.news_autor
        )
        findNavController().navigate(action)
    }
}