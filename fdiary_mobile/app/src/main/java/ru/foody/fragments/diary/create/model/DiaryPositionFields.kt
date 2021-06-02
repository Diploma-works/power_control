package ru.foody.fragments.diary.create.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.ObservableArrayMap
import androidx.databinding.ObservableField
import ru.foody.BR
import ru.foody.R
import ru.guybydefault.foody.domain.NutrientType

class DiaryPositionFields : BaseObservable() {
    var name: String? = null
        set(value) {
            field = value
            // Notify that the valid property could have changed.
            notifyPropertyChanged(BR.valid)
        }
    var date: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.valid)
        }
    var calories: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.valid)
        }
    var protein: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.valid)
        }
    var fats: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.valid)
        }
    var carbs: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.valid)
        }

    var emailError = ObservableField<Int?>()
    var passwordError = ObservableField<Int?>()
    var nutrientsInfo = ObservableArrayMap<NutrientType, Double>()

    @get:Bindable
    val isValid: Boolean
        get() {
            var valid = isEmailValid(false)
            valid = isPasswordValid(false) && valid
            return valid
        }

    fun isEmailValid(setMessage: Boolean): Boolean {
        // Minimum a@b.c
        if (name != null && name!!.length > 5) {
            val indexOfAt = name!!.indexOf("@")
            val indexOfDot = name!!.lastIndexOf(".")
            return if (indexOfAt > 0 && indexOfDot > indexOfAt && indexOfDot < name!!.length - 1) {
                emailError.set(null)
                true
            } else {
                if (setMessage) emailError.set(R.string.error_format_invalid)
                false
            }
        }
        if (setMessage) emailError.set(R.string.error_too_short)
        return false
    }

    fun isPasswordValid(setMessage: Boolean): Boolean {
        return if (date != null && date!!.length > 5) {
            passwordError.set(null)
            true
        } else {
            if (setMessage) passwordError.set(R.string.error_too_short)
            false
        }
    }
}