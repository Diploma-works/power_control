package ru.foody.fragments.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.fragment.app.*
import com.google.android.material.slider.RangeSlider
import retrofit2.Call
import retrofit2.Response
import ru.foody.api.DAO
import ru.foody.R
import kotlin.math.roundToInt


val FILTER_RESULT_REQUEST_KEY = "filterParams";

class RecipeListFilterFragment(private var filterParams: FilterRequestParams) : Fragment() {

    companion object {
        val DUMMY_SPINNER_OPTIONS = "";
    }

    private lateinit var cuisineSpinner: Spinner
    private lateinit var categorySpinner: Spinner

    private lateinit var timePreparedRadioGroup: RadioGroup
    private lateinit var ratingRadioGroup: RadioGroup

    private lateinit var caloriesRangeSlider: RangeSlider

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        val view: View = inflater.inflate(R.layout.fragment_recipes_list_filter, container, false)

        cuisineSpinner = view.findViewById(R.id.spinner_cuisine_selector)
        categorySpinner = view.findViewById(R.id.spinner_category_selector)

        DAO.recipeApi.getAllCuisines()
            .enqueue(spinnerUpdateSelectMenuCallback(cuisineSpinner, filterParams.cuisine))
        DAO.recipeApi.getAllCategories()
            .enqueue(spinnerUpdateSelectMenuCallback(categorySpinner, filterParams.category))

        timePreparedRadioGroup = view.findViewById(R.id.radio_group_time)
        ratingRadioGroup = view.findViewById(R.id.radio_group_rating)

        timePreparedRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radio_time_15m -> filterParams.maxTimePrepared = 15;
                R.id.radio_time_30m -> filterParams.maxTimePrepared = 30;
                R.id.radio_time_1h -> filterParams.maxTimePrepared = 60;
                R.id.radio_time_clear -> filterParams.maxTimePrepared = null;
            }
        }

        ratingRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radio_rating_2 -> filterParams.minRating = 2;
                R.id.radio_rating_3 -> filterParams.minRating = 3;
                R.id.radio_rating_4 -> filterParams.minRating = 4;
                R.id.radio_time_clear -> filterParams.minRating = null;
            }
        }

        caloriesRangeSlider = view.findViewById(R.id.slider_multiple_thumbs);
        caloriesRangeSlider.addOnChangeListener { slider, value, fromUser ->
            filterParams.caloriesMin = slider.values[0].toInt();
            filterParams.caloriesMax = slider.values[1].roundToInt()
        }

        categorySpinner.onItemSelectedListener =
            (object : OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selected =
                        parent!!.findViewById<Spinner>(R.id.spinner_category_selector)?.selectedItem;
                    if (selected == DUMMY_SPINNER_OPTIONS) {
                        filterParams.category = null;
                    } else {
                        filterParams.category = selected.toString();
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    filterParams.category = null;
                }
            })



        cuisineSpinner.onItemSelectedListener =
            (object : OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selected =
                        parent!!.findViewById<Spinner>(R.id.spinner_cuisine_selector)?.selectedItem;
                    if (selected == DUMMY_SPINNER_OPTIONS) {
                        filterParams.cuisine = null;
                    } else {
                        filterParams.cuisine = selected.toString();
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    filterParams.cuisine = null;
                }
            })

        view.findViewById<Button>(R.id.apply_filter_btn).setOnClickListener {
            val filterParamsBundle = Bundle()
            filterParamsBundle.putParcelable(FILTER_RESULT_REQUEST_KEY, filterParams)
            setFragmentResult(FILTER_RESULT_REQUEST_KEY, filterParamsBundle)
        }

        initFilterValues()

        return view
    }

    private fun initFilterValues() {
        when (filterParams.minRating) {
            null -> ratingRadioGroup.check(R.id.radio_rating_clear)
            2 -> ratingRadioGroup.check(R.id.radio_rating_2)
            3 -> ratingRadioGroup.check(R.id.radio_rating_3)
            4 -> ratingRadioGroup.check(R.id.radio_rating_4)
        }

        when (filterParams.maxTimePrepared) {
            null -> timePreparedRadioGroup.check(R.id.radio_time_clear)
            15 -> timePreparedRadioGroup.check(R.id.radio_time_15m)
            30 -> timePreparedRadioGroup.check(R.id.radio_time_30m)
            60 -> timePreparedRadioGroup.check(R.id.radio_time_1h)
        }

        caloriesRangeSlider.setValues(
            filterParams.caloriesMin.toFloat(),
            filterParams.caloriesMax.toFloat()
        )
    }

    private fun spinnerUpdateSelectMenuCallback(
        spinner: Spinner,
        initialValue: String?
    ): retrofit2.Callback<Array<String>> {
        return object : retrofit2.Callback<Array<String>> {
            override fun onResponse(
                call: Call<Array<String>>,
                response: Response<Array<String>>
            ) {
                if (response.isSuccessful) {
                    arrayOf(mutableListOf<Int>())
                    val values: MutableList<String> = mutableListOf<String>("") // default option
                    values.addAll(response.body()!!)
                    val valueArrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
                        activity!!,
                        android.R.layout.simple_spinner_item,
                        values.toTypedArray()
                    )
                    valueArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.adapter = valueArrayAdapter
                    if (initialValue != null) {
                        // for init
                        spinner.setSelection(valueArrayAdapter.getPosition(initialValue))
                    }
                }
            }

            override fun onFailure(call: Call<Array<String>>, t: Throwable) {
                val toast = Toast.makeText(
                    activity!!.applicationContext,
                    "Error loading data: " + t.message!!,
                    Toast.LENGTH_SHORT
                )
                toast.show()
            }
        }
    }

}