package com.jimenez.ecuafit.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.jimenez.ecuafit.data.Comida
import com.jimenez.ecuafit.databinding.ActivityDetailsComidasItemsBinding
import com.squareup.picasso.Picasso
import kotlin.streams.toList

class DetailsComidasItems : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsComidasItemsBinding
    private val macroNut = listOf<String>("Carbs          "
                                        , "Grasas        "
                                        , "Proteinas    ")
    private var count = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsComidasItemsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        val item = intent.getParcelableExtra<Comida>("name")
        if (item != null) {

            binding.macroList.adapter = ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                item.macronutrientes.stream().map {

                    count++
                    macroNut[count-1] + it + " gr"
                }

                    .toList())
            binding.microList.adapter = ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                item.micronutrientes
            )


            binding.nombreComida.text = item.nombre
            binding.calorias.text = item.calorias.toString()
            Picasso.get().load(item.foto).into(binding.imagenComida)
        }
    }

}