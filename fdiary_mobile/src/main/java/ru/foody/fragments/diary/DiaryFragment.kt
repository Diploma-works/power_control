package ru.foody.fragments.diary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.foody.MainActivity
import ru.foody.R
import ru.foody.fragments.diary.create.CreateDiaryPositionFragment
import ru.guybydefault.foody.domain.DiaryPosition


class DiaryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    private lateinit
    var recyclerViewAdapter: DiaryPositionsToViewHolderAdapter
    private lateinit var recyclerViewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        val view: View = inflater.inflate(R.layout.fragment_diary, container, false)

        recyclerView = view.findViewById(R.id.diary_recycler_view)
        recyclerView.isNestedScrollingEnabled = false;

        updateDiaryPositionsList()

        recyclerViewAdapter = DiaryPositionsToViewHolderAdapter(mutableListOf(), this)

        recyclerViewManager = LinearLayoutManager(context)
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = recyclerViewManager
            adapter = recyclerViewAdapter
        }

        return view
    }

    private fun updateDiaryPositionsList() {
        (requireActivity() as MainActivity).diaryService.getDiaryPositionsForCurrentDay() { diaryPositionList ->
            run {
                recyclerViewAdapter.dataSet = diaryPositionList
                recyclerViewAdapter.notifyDataSetChanged()
            }
        }
    }

    internal fun delete(diaryPosition: DiaryPosition) {
        (requireActivity() as MainActivity).diaryService.removeDiaryPosition(diaryPosition) { result ->
            val index = recyclerViewAdapter.dataSet.indexOf(diaryPosition)
            recyclerViewAdapter.dataSet.removeAt(index)
            recyclerViewAdapter.notifyDataSetChanged()
            val toast = Toast.makeText(
                requireActivity().applicationContext,
                "Позиция удалена",
                Toast.LENGTH_SHORT
            )
            toast.show()
        }
    }

    internal fun edit(diaryPosition: DiaryPosition) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fl_wrapper, CreateDiaryPositionFragment(diaryPosition))
            .commit()
    }

}