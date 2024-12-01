package com.dicoding.asclepius.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.adapter.HistoryAdapter
import com.dicoding.asclepius.databinding.FragmentHistoryBinding
import com.dicoding.asclepius.viewmodel.CancerViewModel
import com.dicoding.asclepius.viewmodel.CancerViewModelFactory

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CancerViewModel by viewModels { CancerViewModelFactory(requireActivity().application) }

    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        binding.historyProgressBar.visibility = View.VISIBLE

        viewModel.predictHistory.observe(viewLifecycleOwner) { historyList ->
            binding.historyProgressBar.visibility = View.GONE

            if (historyList != null && historyList.isNotEmpty()) {
                historyAdapter.submitList(historyList)
                binding.emptyStateTextView.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
            } else {
                binding.recyclerView.visibility = View.GONE
                binding.emptyStateTextView.visibility = View.VISIBLE
            }
        }
    }

    private fun setupRecyclerView() {
        historyAdapter = HistoryAdapter { history ->
            viewModel.deletePrediction(history)
        }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = historyAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
