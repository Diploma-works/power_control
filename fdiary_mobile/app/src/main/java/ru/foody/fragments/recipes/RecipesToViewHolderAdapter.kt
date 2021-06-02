package ru.foody.fragments.recipes

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.foody.R
import ru.foody.model.Recipe

class RecipesToViewHolderAdapter(
    var dataSet: Array<Recipe>,
    private val recipeListFragment: RecipeListFragment
) :
    RecyclerView.Adapter<RecipeViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val cardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recipe_view, parent, false) as CardView
        val imageView = cardView.findViewById<ImageView>(R.id.cardview_recipe_image)
        val nameTextView = cardView.findViewById<TextView>(R.id.cardview_recipe_name)
        val recipeTagsView = cardView.findViewById<TextView>(R.id.cardview_recipe_tags)
        val caloriesView: TextView = cardView.findViewById(R.id.calories)
        val proteinView: TextView = cardView.findViewById(R.id.prot_col)
        val carbsView: TextView = cardView.findViewById(R.id.carb_col)
        val fatsView: TextView = cardView.findViewById(R.id.fat_col)
        val ratingView: TextView = cardView.findViewById(R.id.cardview_rating)
        val timePreparedView: TextView = cardView.findViewById(R.id.cardview_time_prepared)
        return RecipeViewHolder(
            cardView,
            imageView,
            nameTextView,
            recipeTagsView,
            caloriesView,
            proteinView,
            carbsView,
            fatsView,
            ratingView,
            timePreparedView
        )
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holderRecipe: RecipeViewHolder, position: Int) {
        val recipe: Recipe = dataSet[position];

        Picasso.get()
            .load(recipe.imageLink)
            .placeholder(R.drawable.recipe_placeholder)
            .error(R.drawable.recipe_placeholder_error)
            .into(holderRecipe.imageView)

        holderRecipe.nameTextView.text = recipe.name
        holderRecipe.recipeTagsView.text = recipe.tags.joinToString(separator = ", ")
        holderRecipe.caloriesView.text =
            recipe.nutrientsInfo.calories.toString()
        holderRecipe.proteinView.text =
            recipe.nutrientsInfo.protein.toString()
        holderRecipe.carbsView.text = recipe.nutrientsInfo.carb.toString()
        holderRecipe.fatsView.text = recipe.nutrientsInfo.fat.toString()
        holderRecipe.ratingView.text = recipe.rating.toString();
        holderRecipe.timePreparedView.text = recipe.minutesPrepared + " минут"
        holderRecipe.itemView.setOnClickListener {
            recipeListFragment.openRecipePage(recipe)
        }
    }
}