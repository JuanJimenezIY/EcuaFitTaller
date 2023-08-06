package com.jimenez.ecuafit.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.jimenez.ecuafit.R
import com.jimenez.ecuafit.data.entities.Comida
import com.jimenez.ecuafit.data.entities.ComidaDB
import com.jimenez.ecuafit.databinding.ActivityComidaDiariaBinding
import com.jimenez.ecuafit.logic.ComidaLogic
import com.jimenez.ecuafit.logic.ComidaLogicDB
import com.jimenez.ecuafit.ui.adapters.ComidaAdapter
import com.jimenez.ecuafit.ui.adapters.ComidaDBAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ComidaDiariaActivity : AppCompatActivity() {
    private lateinit var binding:ActivityComidaDiariaBinding
    private var comidaItems: MutableList<ComidaDB> = mutableListOf();
    private var rvAdapter: ComidaDBAdapter = ComidaDBAdapter ()
    private lateinit var lmanager: LinearLayoutManager




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityComidaDiariaBinding.inflate(layoutInflater)
        lmanager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        setContentView(binding.root)
        chargeData()
    }
    private fun chargeData() {


        lifecycleScope.launch(Dispatchers.Main) {
            // progressBar.visibility = View.VISIBLE
            comidaItems = withContext(Dispatchers.IO) {
                return@withContext ComidaLogicDB().getAllComida()


            } as MutableList<ComidaDB>
            if (comidaItems.size == 0) {
                var f = Snackbar.make(binding.root, "No se encontro", Snackbar.LENGTH_LONG)

                f.show()
            }
            rvAdapter.items = comidaItems







            binding.rvComidas.apply {
                this.adapter = rvAdapter
                //  this.layoutManager = lmanager
                this.layoutManager = lmanager
            }
            // progressBar.visibility = View.GONE


        }
    }
}