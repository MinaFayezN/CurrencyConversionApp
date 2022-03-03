package dev.mina.currency.details

import android.graphics.Color
import android.graphics.DashPathEffect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.*
import com.github.mikephil.charting.components.Legend.LegendForm
import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import dagger.hilt.android.AndroidEntryPoint
import dev.mina.currency.R
import dev.mina.currency.databinding.FragmentDetailsBinding
import dev.mina.currency.utils.observeForSingleEvent


@AndroidEntryPoint
class DetailsFragment : Fragment() {

    /**
     * Using binding with this way is the recommended one by google
     * the old way {private lateinit var binding: FragmentDetailsBinding}
     * leads to memory leaks
     *
     * Please refer to this:
     * @see <a href="https://developer.android.com/topic/libraries/view-binding#fragments">Binding in fragments</a>
     */
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val arguments: DetailsFragmentArgs by navArgs()
    private val viewModel: DetailsViewModel by viewModels()
    private val viewState: DetailsViewState by lazy { DetailsViewState() }

    private val chart: LineChart by lazy { binding.chart }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewState = viewState
        }
        uiObservations()
        createChart()
        return binding.root
    }

    private fun uiObservations() {
        viewModel.loading.observeForSingleEvent(this) {
            viewState.itemsDisabled.set(it)
            if (it) {
                binding.shimmerViewContainer.startShimmer()
            } else {
                binding.shimmerViewContainer.stopShimmer()
            }
        }
        viewModel.timeSeries.observe(viewLifecycleOwner) { response ->
            viewState.historicAdapter.updateData(response)
            setData(response)
        }
        viewModel.otherRates.observe(viewLifecycleOwner) { response ->
            viewState.othersAdapter.updateData(response)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun createChart() {
        run {
            // // Chart Style // //

            // background color
            chart.setBackgroundColor(Color.WHITE)

            // disable description text
            chart.description.isEnabled = false

            // enable touch gestures
            chart.setTouchEnabled(true)

            chart.setDrawGridBackground(false)

            // enable scaling and dragging
            chart.isDragEnabled = true
            chart.setScaleEnabled(true)
            // chart.setScaleXEnabled(true);
            // chart.setScaleYEnabled(true);

            // force pinch zoom along both axis
            chart.setPinchZoom(true)
        }
        var xAxis: XAxis
        run {
            // // X-Axis Style // //
            xAxis = chart.xAxis

            // vertical grid lines
            xAxis.enableGridDashedLine(10f, 10f, 0f)
        }
        var yAxis: YAxis
        run {
            // // Y-Axis Style // //
            yAxis = chart.axisLeft

            // disable dual axis (only use LEFT axis)
            chart.axisRight.isEnabled = false

            // horizontal grid lines
            yAxis.enableGridDashedLine(10f, 10f, 0f)
        }
        // draw points over time
        chart.animateX(1500)

        // get the legend (only possible after setting data)
        val l: Legend = chart.legend

        // draw legend entries as lines
        l.form = LegendForm.LINE
    }

    private fun setData(list: List<HistoricItem>) {

        val values = ArrayList<Entry>()
        for (i in list.indices) {
            values.add(Entry(i.toFloat(), list[i].toRate?.toFloat() ?: 0f,
                ResourcesCompat.getDrawable(resources, android.R.drawable.star_on, null)))
        }
        val set1: LineDataSet
        if (chart.data != null &&
            chart.data.dataSetCount > 0
        ) {
            set1 = chart.data.getDataSetByIndex(0) as LineDataSet
            set1.values = values
            set1.notifyDataSetChanged()
            chart.data.notifyDataChanged()
            chart.notifyDataSetChanged()
        } else {
            // create a dataset and give it a type
            set1 = LineDataSet(values, "${arguments.base} to ${arguments.to}")
            set1.setDrawIcons(false)

            // draw dashed line
            set1.enableDashedLine(10f, 5f, 0f)

            // black lines and points
            set1.color = Color.BLACK
            set1.setCircleColor(Color.BLACK)

            // line thickness and point size
            set1.lineWidth = 1f
            set1.circleRadius = 3f


            // draw points as solid circles
            set1.setDrawCircleHole(false)

            // customize legend entry
            set1.formLineWidth = 1f
            set1.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
            set1.formSize = 15f

            // text size of values
            set1.valueTextSize = 9f

            // draw selection line as dashed
            set1.enableDashedHighlightLine(10f, 5f, 0f)

            // set the filled area
            set1.setDrawFilled(true)
            set1.fillFormatter =
                IFillFormatter { _, _ -> chart.axisLeft.axisMinimum }

            val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.fade_red)
            set1.fillDrawable = drawable

            val dataSets = ArrayList<ILineDataSet>()
            dataSets.add(set1) // add the data sets

            // create a data object with the data sets
            val data = LineData(dataSets)

            // set data
            chart.data = data
        }
    }


}