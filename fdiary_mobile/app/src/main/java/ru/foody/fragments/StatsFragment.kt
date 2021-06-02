package ru.foody.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import ru.foody.MainActivity
import ru.foody.R
import ru.guybydefault.foody.domain.DiaryPosition
import ru.guybydefault.foody.domain.NutrientType
import ru.guybydefault.foody.domain.NutrientsInfo


class StatsFragment(val diaryPositions: List<DiaryPosition>) : Fragment() {
    lateinit var totalNutrientsInfo: NutrientsInfo


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        totalNutrientsInfo =
            (requireActivity() as MainActivity).diaryService.getNutrientsTotal(diaryPositions)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_stats, container, false)
        setupMacroNutrientsPieChart(view)
        setupMicroNutrientsChart(view)

        return view
    }

    private fun setupMicroNutrientsChart(view: View) {
        val chartView = view.findViewById<View>(R.id.micronutrients_bar_chart) as HorizontalBarChart

        (requireActivity() as MainActivity).diaryService.getNutrientTypes { full: List<NutrientType> ->
            val barEntries = mutableListOf<BarEntry>()

            for (i in 0 until full.size) {
                val nutrientType = full.get(i)
                barEntries.add(
                    BarEntry(
                        i.toFloat(),
                        totalNutrientsInfo.getNutrientByType(nutrientType).value.toFloat()
                    )
                )
            }

            val nutrientDataSet = BarDataSet(barEntries, "Label t")
            nutrientDataSet.setColors(ColorTemplate.MATERIAL_COLORS.asList())

            nutrientDataSet.valueFormatter = object : ValueFormatter() {
                override fun getBarLabel(barEntry: BarEntry?): String {
                    val nutrientType = full.get(barEntry!!.x.toInt())
                    return nutrientType.name
                }

                override fun getFormattedValue(value: Float): String {
                    return "label $value"
                }
            }

            chartView.data = BarData(nutrientDataSet)
//            chartView.xAxis.valueFormatter = object : ValueFormatter() {
//                override fun getBarLabel(barEntry: BarEntry?): String {
//                    return "bar label (xaxis)"
//                }
//
//                override fun getAxisLabel(value: Float, axis: AxisBase?): String {
//                    if (value >= 0 && value < full.size) {
//                        val nutrientType = full.get(value.toInt())
//                        return nutrientType.name
//                    } else {
//                        return ""
//                    }
//                }
//            }
//            chartView.xAxis.granularity = 0.5f
//            chartView.xAxis.spaceMin = 1f
//            chartView.xAxis.labelCount = full.size
//            chartView.xAxis.disableAxisLineDashedLine()
//            chartView.xAxis.disableGridDashedLine()
////            chartView.xAxis.setCenterAxisLabels(true)
//            chartView.xAxis.setDrawLabels(true)
            chartView.xAxis.setDrawLabels(false)

            chartView.apply {
//                isNestedScrollingEnabled = false
                isScaleXEnabled = false
                isScaleYEnabled = false

                setFitBars(true)

                axisLeft.axisMinimum = 0f
                axisLeft.axisMaximum = 100f
                axisRight.axisMinimum = 0f
                axisRight.axisMaximum = 100f

                invalidate()
            }
        }


    }

    private fun setupMacroNutrientsPieChart(view: View) {
        val chartView = view.findViewById<View>(R.id.macronutrients_pie_chart) as PieChart

        val pieEntries = mutableListOf<PieEntry>()
        pieEntries.add(PieEntry(totalNutrientsInfo.carb.toFloat(), "Углеводы"))
        pieEntries.add(PieEntry(totalNutrientsInfo.fat.toFloat(), "Жиры"))
        pieEntries.add(PieEntry(totalNutrientsInfo.protein.toFloat(), "Белки"))

        val pieDataSet = PieDataSet(pieEntries, "Макронутриенты")
        pieDataSet.colors = mutableListOf(Color.rgb(0, 0, 187), Color.rgb(0, 155, 0), Color.rgb(189, 0, 0))
        val data = PieData(pieDataSet)

        data.setValueTextSize(18f)
        data.setValueTextColor(Color.rgb(255, 255, 25))

        chartView.apply {
            description.text = "Диаграмма макронутриентов"
            this.data = data
            invalidate()
        }
    }

}