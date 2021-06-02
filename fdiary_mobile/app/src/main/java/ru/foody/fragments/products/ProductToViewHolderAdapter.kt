package ru.foody.fragments.products

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import ru.foody.R
import ru.guybydefault.foody.domain.Product

class ProductToViewHolderAdapter(
    var productsList: List<Product>,
    private val searchProductFragment: SearchProductFragment
) :
    RecyclerView.Adapter<ProductViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val cardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_view, parent, false) as CardView
        val nameTextView = cardView.findViewById<TextView>(R.id.cardview_recipe_name)
        val caloriesView: TextView = cardView.findViewById(R.id.calories)
        val proteinView: TextView = cardView.findViewById(R.id.prot_col)
        val carbsView: TextView = cardView.findViewById(R.id.carb_col)
        val fatsView: TextView = cardView.findViewById(R.id.fat_col)
        return ProductViewHolder(
            cardView,
            nameTextView,
            caloriesView,
            proteinView,
            carbsView,
            fatsView
        )
    }

    override fun getItemCount(): Int {
        return productsList.size
    }

    override fun onBindViewHolder(holderRecipe: ProductViewHolder, position: Int) {
        val product: Product = productsList[position]

        holderRecipe.nameTextView.text = product.name
        holderRecipe.caloriesView.text = product.nutrients.calories.toString()
        holderRecipe.proteinView.text = product.nutrients.protein.toString()
        holderRecipe.carbsView.text = product.nutrients.carb.toString()
        holderRecipe.fatsView.text = product.nutrients.fat.toString()
        holderRecipe.itemView.setOnClickListener {
            val addProductDialog = SearchProductFragment.ProductAmountDialog(product)
            val fmTransaction = searchProductFragment.childFragmentManager.beginTransaction()
            addProductDialog.show(
                fmTransaction,
                "add"
            )
        }
    }
}