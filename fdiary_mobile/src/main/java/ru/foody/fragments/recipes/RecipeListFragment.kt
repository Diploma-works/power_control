package ru.foody.fragments.recipes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.foody.MainActivity
import ru.foody.R
import ru.foody.model.Recipe

class RecipeListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecipesToViewHolderAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var scrollView: NestedScrollView

    private var filterRequestParams: FilterRequestParams = FilterRequestParams()

    private var recipeListFilterFragment: RecipeListFilterFragment? = null;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        val view: View = inflater.inflate(R.layout.fragment_recipes_list, container, false)

        scrollView = view.findViewById(R.id.recipes_scroll_view)

        recyclerView = view.findViewById(R.id.recipes_recycler_view)
        recyclerView.isNestedScrollingEnabled = false;

        viewAdapter = RecipesToViewHolderAdapter(emptyArray(), this)

        viewManager = LinearLayoutManager(context)
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        listenToFilterParamsForm()
        initShowFilterBtn(view)
        updateRecipeList()

        return view
    }

    fun openRecipePage(recipe: Recipe) {
        //TODO
//        viewModel.recipe = recipe
//        (activity as MainActivity).setCurrentFragment(SingleRecipe())
    }

    private fun updateRecipeList() {
        (requireActivity() as MainActivity).diaryService.getRecipes(
            filterRequestParams.maxTimePrepared,
            filterRequestParams.minRating,
            filterRequestParams.caloriesMin,
            filterRequestParams.caloriesMax,
            filterRequestParams.cuisine,
            filterRequestParams.category
        )
        { recipes: MutableList<Recipe> ->
            viewAdapter.dataSet = recipes.toTypedArray()
            viewAdapter.notifyDataSetChanged();
        }
    }

    private fun listenToFilterParamsForm() {
        childFragmentManager.setFragmentResultListener(
            FILTER_RESULT_REQUEST_KEY,
            this
        ) { requestKey, bundle ->
            filterRequestParams =
                bundle.getParcelable(FILTER_RESULT_REQUEST_KEY)!!
            updateRecipeList()
            childFragmentManager
                .beginTransaction().remove(recipeListFilterFragment!!)
                .commit()
        }
    }

    private fun initShowFilterBtn(mainView: View) {
        val btn: Button = mainView.findViewById(R.id.filter_btn)
        btn.setOnClickListener {
            scrollView.scrollTo(0, 0);
            recipeListFilterFragment = RecipeListFilterFragment(filterRequestParams)
            childFragmentManager.beginTransaction().apply {
                replace(R.id.filter_layout, recipeListFilterFragment!!)
                commit()
            }
        }
    }

    private fun err(msg: String) {
        val toast = Toast.makeText(
            requireActivity().applicationContext,
            "Error loading data: " + msg,
            Toast.LENGTH_SHORT
        )
        toast.show()
    }
}