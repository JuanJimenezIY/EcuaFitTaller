package com.jimenez.ecuafit.ui.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.jimenez.ecuafit.R
import com.jimenez.ecuafit.data.entities.Comida
import com.jimenez.ecuafit.data.entities.Ejercicio
import com.jimenez.ecuafit.databinding.ActivityEjerciciosBinding
import com.jimenez.ecuafit.logic.ComidaLogic
import com.jimenez.ecuafit.logic.EjercicioLogic
import com.jimenez.ecuafit.ui.adapters.ComidaAdapter
import com.jimenez.ecuafit.ui.adapters.EjercicioAdapter
import com.jimenez.ecuafit.ui.fragments.DialogFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext



class EjerciciosActivity : AppCompatActivity() {
    private lateinit var binding:ActivityEjerciciosBinding
    private lateinit var lmanager: LinearLayoutManager
    private var ejercicioItems: MutableList<Ejercicio> = mutableListOf();

    //  private lateinit var progressBar:ProgressBar
    private var rvAdapter: EjercicioAdapter = EjercicioAdapter (::openVideo )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityEjerciciosBinding.inflate(layoutInflater)
        lmanager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
chargeData()
        setContentView(binding.root)
    }
    fun openVideo(ejercicio: Ejercicio) {
val intent=Intent(Intent.ACTION_VIEW, Uri.parse(ejercicio.video))
    startActivity(intent)}

    override fun onStart() {
        super.onStart()
    }
    private fun chargeData() {


        lifecycleScope.launch(Dispatchers.Main) {
            // progressBar.visibility = View.VISIBLE
            ejercicioItems = withContext(Dispatchers.IO) {
                return@withContext EjercicioLogic().getAllEjercicios()


            } as MutableList<Ejercicio>
            if (ejercicioItems.size == 0) {
                var f = Snackbar.make(binding.textView4, "No se encontro", Snackbar.LENGTH_LONG)

                f.show()
            }
            rvAdapter.items = ejercicioItems







            binding.rvEjercicios.apply {
                this.adapter = rvAdapter
                //  this.layoutManager = lmanager
                this.layoutManager = lmanager
            }
            // progressBar.visibility = View.GONE


        }
    }

}