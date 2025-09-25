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
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

private val categories = listOf(
    null to "Все",
    "vegetarian" to "Vegetarian",
    "dessert" to "Dessert",
    "breakfast" to "Breakfast",
    "lunch" to "Lunch",
    "dinner" to "Dinner"
)


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

        categories.forEach { (tag, label) ->
            val chip = Chip(requireContext()).apply {
                text = label
                isCheckable = true
                setOnClickListener {
                    viewLifecycleOwner.lifecycleScope.launch {
                        viewModel.loadByCategory(tag)
                            .collect { list ->
                                adapter.submitList(list)
                                binding.recyclerRecipes.scrollToPosition(0)
                            }
                    }
                }
            }
            binding.chipGroupCategories.addView(chip)
        }


        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.containerLoading.visibility = if (loading) View.VISIBLE else View.GONE
            binding.recyclerRecipes.visibility = if (loading) View.GONE else View.VISIBLE
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.recipes.collect { list ->
                adapter.submitList(list)
                binding.recyclerRecipes.scrollToPosition(0)
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
                .flatMapLatest { query ->
                    if (query.isBlank()) {
                        viewModel.loadRandom()
                    } else {
                        binding.chipGroupCategories.clearCheck()
                        (binding.chipGroupCategories.getChildAt(0) as Chip).isChecked = true
                        viewModel.search(query)
                    }
                }
                .collect { list ->
                    adapter.submitList(list)
                    binding.recyclerRecipes.scrollToPosition(0)

                    if (list.isEmpty()) {
                        binding.recyclerRecipes.visibility = View.GONE
                        binding.emptyContainer.visibility = View.VISIBLE
                    } else {
                        binding.recyclerRecipes.visibility = View.VISIBLE
                        binding.emptyContainer.visibility = View.GONE
                    }
                }
        }
        (binding.chipGroupCategories.getChildAt(0) as Chip).isChecked = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}