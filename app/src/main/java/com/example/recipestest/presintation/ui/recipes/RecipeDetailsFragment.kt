package com.example.recipestest.presintation.ui.recipes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.recipestest.databinding.FragmentRecipesDetailsBinding
import com.example.recipestest.presintation.utils.toSpanned
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue


@AndroidEntryPoint
class RecipeDetailsFragment : Fragment() {

    private var _binding: FragmentRecipesDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RecipeDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = RecipeDetailsFragmentArgs.fromBundle(requireArguments())
        viewModel.loadDetails(args.recipeId)

        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.mainContainer.visibility = if (loading) View.GONE else View.VISIBLE
            binding.containerLoading.visibility = if (loading) View.VISIBLE else View.GONE
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.details.collect { recipe ->
                recipe?.let {
                    Log.d("recipeDetails", "${recipe}")
                    binding.titleRecipe.text = it.title
                    binding.descriptionRecipe.text = it.summary?.toSpanned()
                    binding.servingsText.text = "${it.servings} persons"
                    binding.readyInMinutesText.text = "${it.readyInMinutes} minutes"
                    Glide.with(requireContext())
                        .load(it.image)
                        .into(binding.imageRecipe)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
