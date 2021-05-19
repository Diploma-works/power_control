package ru.foody.fragments.diary.create

import android.text.Editable
import android.text.TextWatcher
import android.view.View.OnFocusChangeListener
import android.widget.EditText
import androidx.databinding.Bindable
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.android.synthetic.main.fragment_create_diary_position.*
import ru.foody.fragments.diary.create.model.DiaryPositionFields
import ru.foody.service.DiaryService
import ru.guybydefault.foody.domain.NutrientType

class CreateDiaryPositionViewModel() : ViewModel() {
    lateinit var diaryService: DiaryService

    var diaryPosition: DiaryPositionFields? = null
        private set
    var emailOnFocusChangeListener: OnFocusChangeListener? = null
        private set
    var passwordOnFocusChangeListener: OnFocusChangeListener? = null
        private set
    val buttonClick = MutableLiveData<DiaryPositionFields?>()

    var fullList: MutableLiveData<MutableList<NutrientType>> = MutableLiveData(mutableListOf())
    var filteredList: MutableLiveData<MutableList<NutrientType>> = MutableLiveData(mutableListOf())

    fun init(diaryService: DiaryService) {
        this.diaryService = diaryService
        diaryPosition = DiaryPositionFields()
        emailOnFocusChangeListener = OnFocusChangeListener { view, focused ->
            val et = view as EditText
            if (et.text.length > 0 && !focused) {
                diaryPosition!!.isEmailValid(true)
            }
        }
        passwordOnFocusChangeListener = OnFocusChangeListener { view, focused ->
            val et = view as EditText
            if (et.text.length > 0 && !focused) {
                diaryPosition!!.isPasswordValid(true)
            }
        }
        search("")
    }

    fun onButtonClick() {
        if (diaryPosition!!.isValid) {
            buttonClick.value = diaryPosition
        }
    }

    fun onSearch(edible: String?) {
        if (edible.isNullOrBlank()) {
            filteredList.value = fullList.value
        } else {
            search(edible!!)
        }
    }

    private fun search(edible: String) {
        diaryService.searchNutrientTypes(edible) { full, filtered ->
            fullList.value = full
            filteredList.value = filtered
        }
    }

    fun getSearchNutrientTypesFieldTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(edible: Editable?) {
                onSearch(edible?.toString())
            }
        }
    }
}

@BindingAdapter("error")
fun setError(editText: EditText, strOrResId: Any?) {
    if (strOrResId is Int) {
        editText.error = editText.context.getString((strOrResId as Int?)!!)
    } else {
        editText.error = strOrResId as String?
    }
}

@BindingAdapter("onFocus")
fun bindFocusChange(editText: EditText, onFocusChangeListener: OnFocusChangeListener?) {
    if (editText.onFocusChangeListener == null) {
        editText.onFocusChangeListener = onFocusChangeListener
    }
}

@BindingAdapter("textChangedListener")
fun bindTextWatcher(editText: EditText, textWatcher: TextWatcher) {
    editText.addTextChangedListener(textWatcher)
}
