package ru.foody.fragments.recipes

import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class RecipeViewHolder(
    cardView: CardView,
    val imageView: ImageView,
    val nameTextView: TextView,
    val recipeTagsView: TextView,
    val caloriesView: TextView,
    val proteinView: TextView,
    val carbsView: TextView,
    val fatsView: TextView,
    val ratingView: TextView,
    val timePreparedView: TextView
) : RecyclerView.ViewHolder(cardView) {
}