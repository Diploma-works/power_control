package ru.foody.fragments.diary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.foody.DummyObjectsGenerationUtility.roundForDiaryDisplay
import ru.foody.R
import ru.guybydefault.foody.domain.DiaryPosition

class DiaryPositionsToViewHolderAdapter(
    var dataSet: MutableList<DiaryPosition>,
    private val diaryFragment: DiaryFragment
) :
    RecyclerView.Adapter<DiaryPositionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryPositionViewHolder {
        val cardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.diary_position_view, parent, false) as LinearLayout
        val nameTextView = cardView.findViewById<TextView>(R.id.diary_position_name)
        val caloriesView: TextView = cardView.findViewById(R.id.calories)
        val proteinView: TextView = cardView.findViewById(R.id.prot_col)
        val carbsView: TextView = cardView.findViewById(R.id.carb_col)
        val fatsView: TextView = cardView.findViewById(R.id.fat_col)
        val gramsAmountView: TextView = cardView.findViewById(R.id.grams_amount)
        val deleteBtn: ImageButton = cardView.findViewById(R.id.delete_diary_position_btn)
        val editBtn: ImageButton = cardView.findViewById(R.id.edit_diary_position_btn)
        return DiaryPositionViewHolder(
            cardView,
            nameTextView,
            gramsAmountView,
            caloriesView,
            proteinView,
            carbsView,
            fatsView,
            deleteBtn,
            editBtn
        )
    }

    override fun getItemCount(): Int {
        return dataSet.size + 1 // plus header row
    }

    private fun initHeaderCellText(diaryPositionVH: DiaryPositionViewHolder) {
        diaryPositionVH.nameTextView.text = "Название"
        diaryPositionVH.gramsAmountTextView.text = "кол-во (гр)"
        diaryPositionVH.caloriesView.text = "ккал"
        diaryPositionVH.proteinView.text = "Б (гр)"
        diaryPositionVH.fatsView.text = "Ж (гр)"
        diaryPositionVH.carbsView.text = "У (гр)"

        diaryPositionVH.deleteBtn.visibility = View.GONE
        diaryPositionVH.editBtn.visibility = View.GONE
    }

    private fun initContentCellText(
        diaryPositionVH: DiaryPositionViewHolder,
        diaryPosition: DiaryPosition
    ) {
        diaryPositionVH.nameTextView.text = diaryPosition.name
        diaryPositionVH.gramsAmountTextView.text = diaryPosition.value.toString()
        diaryPositionVH.caloriesView.text =
            diaryPosition.totalNutrientsInfo.calories.roundForDiaryDisplay().toString()
        diaryPositionVH.proteinView.text =
            diaryPosition.totalNutrientsInfo.protein.roundForDiaryDisplay().toString()
        diaryPositionVH.carbsView.text =
            diaryPosition.totalNutrientsInfo.carb.roundForDiaryDisplay().toString()
        diaryPositionVH.fatsView.text =
            diaryPosition.totalNutrientsInfo.fat.roundForDiaryDisplay().toString()

        diaryPositionVH.deleteBtn.setOnClickListener {
            diaryFragment.delete(diaryPosition)
        }

        diaryPositionVH.editBtn.setOnClickListener {
            diaryFragment.edit(diaryPosition)
        }
    }


    override fun onBindViewHolder(diaryPositionVH: DiaryPositionViewHolder, position: Int) {
        if (position == 0) {
            initHeaderCellText(diaryPositionVH)
        } else {
            initContentCellText(diaryPositionVH, dataSet.get(position - 1))
        }
        for (cell in mutableListOf<View>
            (
            diaryPositionVH.nameTextView,
            diaryPositionVH.gramsAmountTextView,
            diaryPositionVH.caloriesView,
            diaryPositionVH.proteinView,
            diaryPositionVH.fatsView,
            diaryPositionVH.carbsView
        )) {
            if (position == 0) {
//                setHeaderBg(cell)
            } else {
//                setContentBg(cell)
            }
        }
    }
}