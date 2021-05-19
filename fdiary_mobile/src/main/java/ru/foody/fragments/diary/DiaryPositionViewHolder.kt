package ru.foody.fragments.diary

import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DiaryPositionViewHolder(
    cardView: LinearLayout,
    val nameTextView: TextView,
    val gramsAmountTextView: TextView,
    val caloriesView: TextView,
    val proteinView: TextView,
    val carbsView: TextView,
    val fatsView: TextView,
    val deleteBtn: ImageButton,
    val editBtn: ImageButton
) : RecyclerView.ViewHolder(cardView) {
}