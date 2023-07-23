package com.jimenez.ecuafit.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jimenez.ecuafit.R
import com.jimenez.ecuafit.data.Comida
import com.jimenez.ecuafit.databinding.ActivityDetailsComidasItemsBinding
import com.squareup.picasso.Picasso

class DetailsComidasItems : AppCompatActivity() {
    private lateinit var binding:ActivityDetailsComidasItemsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDetailsComidasItemsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        val item=intent.getParcelableExtra<Comida>("name")
        if(item!=null){
            binding.Macronutrientes.text=item.macronutrientes
            binding.nombreComida.text=item.nombre
            binding.calorias.text=item.calorias.toString()
            Picasso.get().load(item.foto).into(binding.imagenComida)
        }
    }

}