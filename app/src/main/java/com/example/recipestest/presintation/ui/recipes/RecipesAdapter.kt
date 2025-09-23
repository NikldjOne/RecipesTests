package com.example.recipestest.presintation.ui.recipes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipestest.databinding.ItemRecipeBinding
import com.example.recipestest.domain.model.Recipe
import com.example.recipestest.presintation.utils.toSpanned

class RecipesAdapter : ListAdapter<Recipe, RecipesAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<Recipe>() {
        override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe) = oldItem == newItem
    }
) {
    class ViewHolder(val binding: ItemRecipeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = getItem(position)
        holder.binding.title.text = recipe.title
        holder.binding.description.text = recipe.summary?.toSpanned()
        Glide.with(holder.itemView).load(recipe.image).into(holder.binding.image)
        holder.itemView.setOnClickListener {
            val action = RecipesFragmentDirections
                .actionRecipesToDetails(recipe.id)
            it.findNavController().navigate(action)
        }
    }
}
