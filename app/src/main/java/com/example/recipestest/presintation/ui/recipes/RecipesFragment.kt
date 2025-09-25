package com.example.recipestest.presintation.ui.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipestest.databinding.FragmentRecipesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipesFragment : Fragment() {

    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RecipesViewModel by viewModels()
    private lateinit var adapter: RecipesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        return binding.root
    }

    @OptIn(FlowPreview::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = RecipesAdapter()
        binding.recyclerRecipes.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerRecipes.adapter = adapter

        viewModel.isLoading.observe(viewLifecycleOwner){loading ->
            binding.containerLoading.visibility = if(loading) View.VISIBLE else View.GONE
            binding.recyclerRecipes.visibility = if (loading) View.GONE else View.VISIBLE
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.recipes.collect { list ->
                adapter.submitList(list)
            }
        }
        val searchFlow = callbackFlow {
            binding.inputSearch.doOnTextChanged { text, _, _, _ ->
                trySend(text.toString())
            }
            awaitClose { }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            searchFlow
                .debounce(1000)
                .filter { it.isNotBlank() }
                .collect { query ->
                    viewModel.search(query).collect { list ->
                        adapter.submitList(list)
                    }
                }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}