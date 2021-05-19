package ru.foody.fragments.diary.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_create_diary_position.*
import ru.foody.MainActivity
import ru.foody.R
import ru.foody.databinding.FragmentCreateDiaryPositionBinding
import ru.guybydefault.foody.domain.DiaryPosition
import ru.guybydefault.foody.domain.NutrientType

class CreateDiaryPositionFragment(
    val diaryPosition: DiaryPosition
) : Fragment() {
    private var viewModel: CreateDiaryPositionViewModel? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: ToViewHolderAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        val binding = FragmentCreateDiaryPositionBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModel = ViewModelProvider(
            this,
            NewInstanceFactory()
        ).get(CreateDiaryPositionViewModel::class.java)
        if (savedInstanceState == null) {
            viewModel!!.init((requireActivity() as MainActivity).diaryService)
        }
        binding.model = viewModel

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNutrientTypesRecyclerView()
        linkButtonClickToViewModel()
        linkSearchToViewModel()
    }

    private fun setupNutrientTypesRecyclerView() {
        val recyclerView = searched_nutrient_types_recycler_view
        viewManager = LinearLayoutManager(context)
        viewAdapter = ToViewHolderAdapter(
            viewModel!!.filteredList.value!!, this
        )
        recyclerView.apply {
            isNestedScrollingEnabled = false
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    private fun searchNutrientTypes() {
        viewAdapter.nutrientTypeList = viewModel!!.filteredList.value!!
        viewAdapter.notifyDataSetChanged()
    }


    private fun linkSearchToViewModel() {
        viewModel!!.filteredList.observe(viewLifecycleOwner, { filtered ->
            searchNutrientTypes()
        })
    }

    private fun linkButtonClickToViewModel() {
        viewModel!!.buttonClick.observe(viewLifecycleOwner, { loginModel ->
            Toast.makeText(
                this@CreateDiaryPositionFragment.requireContext(),
                "Email " + loginModel!!.name.toString() + ", Password " + loginModel.date,
                Toast.LENGTH_SHORT
            ).show()
        })
    }

    class ToViewHolderAdapter(
        var nutrientTypeList: List<NutrientType>,
        val createDiaryPositionFragment: CreateDiaryPositionFragment
    ) : RecyclerView.Adapter<ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.nutrient_type_view, parent, false)
            return ViewHolder(view, view.findViewById<TextView>(R.id.nutrient_type_name))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.nutrientTypeName.text = nutrientTypeList.get(position).name
        }

        override fun getItemCount(): Int {
            return nutrientTypeList.size
        }
    }

    class ViewHolder(
        val itemView: View,
        val nutrientTypeName: TextView
    ) : RecyclerView.ViewHolder(itemView) {

    }
}