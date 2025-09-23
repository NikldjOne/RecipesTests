package com.example.recipestest.presintation.ui.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.recipestest.databinding.FragmentRecipesDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue


@AndroidEntryPoint
class RecipeDetailsFragment : Fragment() {

    private var _binding: FragmentRecipesDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RecipesViewModel by viewModels()
    private lateinit var adapter: RecipesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = RecipesAdapter()
//        binding.recyclerRecipes.adapter = adapter
//
//        viewLifecycleOwner.lifecycleScope.launch {
//            viewModel.recipes.collectLatest { adapter.submitList(it) }
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}