package ru.foody.fragments.products

import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class ProductViewHolder(
    cardView: CardView,
    val nameTextView: TextView,
    val caloriesView: TextView,
    val proteinView: TextView,
    val carbsView: TextView,
    val fatsView: TextView
) : RecyclerView.ViewHolder(cardView) {
}