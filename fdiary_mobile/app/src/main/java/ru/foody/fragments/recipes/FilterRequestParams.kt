package ru.foody.fragments.recipes

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class FilterRequestParams() : Parcelable {

    companion object {
        val MIN_CALORIES = 0;
        val MAX_CALORIES = 900;
    }

    var maxTimePrepared: Int? = null
    var minRating: Int? = null;
    var caloriesMin: Int = MIN_CALORIES;
    var caloriesMax: Int = MAX_CALORIES;
    var cuisine: String? = null
    var category: String? = null

    override fun toString(): String {
        return "FilterRequestParams(timePrepared=$maxTimePrepared, rating=$minRating, caloriesMin=$caloriesMin, caloriesMax=$caloriesMax, cuisine=$cuisine, category=$category)"
    }

}