package com.jimenez.ecuafit.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.jimenez.ecuafit.R
import com.jimenez.ecuafit.data.Comida
import com.jimenez.ecuafit.databinding.FragmentDiarioBinding
import com.jimenez.ecuafit.logic.ComidaLogic
import com.jimenez.ecuafit.ui.activities.DetailsComidasItems
import com.jimenez.ecuafit.ui.adapters.ComidaAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DiarioFragment : Fragment() {
    private lateinit var binding:FragmentDiarioBinding
    private lateinit var lmanager:LinearLayoutManager
    private var comidaItems:MutableList<Comida> = mutableListOf();
  //  private lateinit var progressBar:ProgressBar
    private var rvAdapter:ComidaAdapter=ComidaAdapter{sendComidaItem(it)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentDiarioBinding.inflate(layoutInflater,container,false)
        lmanager= LinearLayoutManager(requireActivity(),LinearLayoutManager.VERTICAL,false)
        binding.rvComidas.addOnScrollListener(

            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy > 0) {
                        val v = lmanager.childCount
                        val p = lmanager.findFirstVisibleItemPosition()
                        val t = lmanager.itemCount
                    }
                }
            }
        )
        //progressBar = binding.progressBar
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        chargeData()
    }

    fun sendComidaItem(item: Comida){

        val i = Intent(requireActivity(), DetailsComidasItems::class.java)
        i.putExtra("name", item)
        startActivity(i)
    }
    private fun chargeData() {


        lifecycleScope.launch(Dispatchers.Main) {
           // progressBar.visibility = View.VISIBLE
            comidaItems = withContext(Dispatchers.IO) {
                return@withContext ComidaLogic().getAllComida()



            } as MutableList<Comida>
            if (comidaItems.size == 0) {
                var f = Snackbar.make(binding.titulo, "No se encontro", Snackbar.LENGTH_LONG)

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