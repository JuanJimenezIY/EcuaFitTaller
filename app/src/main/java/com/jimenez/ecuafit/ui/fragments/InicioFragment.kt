package com.jimenez.ecuafit.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.jimenez.ecuafit.databinding.FragmentInicioBinding
import com.jimenez.ecuafit.ui.utilities.EcuaFit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class InicioFragment : Fragment() {
    private lateinit var binding:FragmentInicioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= FragmentInicioBinding.inflate(layoutInflater)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch(Dispatchers.Main){
            chargeData()

        }
    }
    suspend fun chargeData(){
        val line = AnyChart.line()

        line.animation(true)
       // line.crosshair().enabled(true)
        line.title("Historial de peso")
        //line.tooltip().positionMode(TooltipPositionMode.POINT)
        var peso= withContext(Dispatchers.IO){
            return@withContext EcuaFit.getDbUsuarioInstance().usuarioDao().getAll().peso
        }
        val data: MutableList<DataEntry> = ArrayList()
        val maxVisible=10
        peso.takeLast(maxVisible).forEachIndexed {i,u->
            data.add(ValueDataEntry(i,u.toDouble()))
        }

        line.data(data)
        binding.anyChartView.setChart(line)

        if(peso.size>=2){
            binding.txtPesoInicio.text=(peso.last().toDouble()-peso[peso.size-2].toDouble()).toString()

        }
    }


}