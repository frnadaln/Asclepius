package com.dicoding.asclepius.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.adapter.CancerAdapter
import com.dicoding.asclepius.databinding.FragmentNewsBinding
import com.dicoding.asclepius.viewmodel.CancerViewModel

class NewsFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!

    private val cancerViewModel: CancerViewModel by viewModels()
    private lateinit var cancerAdapter: CancerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        binding.newsProgressBar.visibility = View.VISIBLE
        binding.emptyStateTextView.visibility = View.GONE
        val apiKey = "002f51e9a885416da1358ad01d5e5698"
        cancerViewModel.fetchCancerNews(apiKey)

        cancerViewModel.newsData.observe(viewLifecycleOwner) { newsResponse ->

            binding.newsProgressBar.visibility = View.GONE

            if (newsResponse != null && newsResponse.articles.isNotEmpty()) {
                cancerAdapter.submitList(newsResponse.articles)
            } else {
                binding.emptyStateTextView.visibility = View.VISIBLE
            }
        }
    }

    private fun setupRecyclerView() {
        cancerAdapter = CancerAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cancerAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
