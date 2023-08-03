package com.jimenez.ecuafit.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.jimenez.ecuafit.R
import com.jimenez.ecuafit.databinding.ActivityAguaBinding

class AguaActivity : AppCompatActivity() {

    private lateinit var binding:ActivityAguaBinding
    private var agua=0
    private var vaso=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAguaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.totalAgua.text="$agua ml"
        binding.totalVasos.text="$vaso"
        binding.botonMas.setOnClickListener{
            agregarAgua()
        }
        binding.botonMenos.setOnClickListener {
            restarAgua()
        }
    }
    fun agregarAgua(){
        agua+=250
        vaso+=1
        actualizarAgua()
    }

    fun restarAgua(){
        if(agua>=250 && vaso>=1){
            agua-=250
            vaso-=1
        }
        actualizarAgua()
    }

    private fun actualizarAgua(){
        binding.totalAgua.text="$agua ml"
        binding.totalVasos.text="$vaso"
    }
}