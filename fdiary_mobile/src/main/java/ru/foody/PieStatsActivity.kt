package ru.foody

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import java.util.*


class PieStatsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.macronutrients_pie_chart)

        val anyChartView = findViewById<View>(R.id.macronutrients_pie_chart) as AnyChartView
        anyChartView.setProgressBar(findViewById(R.id.macronutrients_progress_bar))

        val data: MutableList<DataEntry> = ArrayList()
        data.add(ValueDataEntry("Белки", 120))
        data.add(ValueDataEntry("Жиры", 70))
        data.add(ValueDataEntry("Углеводы", 210))

        val pie = AnyChart.pie()
        pie.credits().enabled(false)
        pie.data(data)
        pie.title("Статистика по макронутриентам")
        anyChartView.setChart(pie)
    }
}