package com.jimenez.ecuafit.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.jimenez.ecuafit.R
import com.jimenez.ecuafit.data.entities.Comida
import com.jimenez.ecuafit.databinding.ActivityMenuBinding
import com.jimenez.ecuafit.ui.fragments.DiarioFragment
import com.jimenez.ecuafit.ui.fragments.InformeFragment
import com.jimenez.ecuafit.ui.fragments.InicioFragment
import com.jimenez.ecuafit.ui.utilities.FragmentsManager


class MenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuBinding
    private lateinit var intent:Intent
    private var usuarioItems: MutableList<String> = mutableListOf();
    private val db = FirebaseFirestore.getInstance()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        intent=getIntent()
        FragmentsManager().replaceFragment(
            supportFragmentManager,binding.frmContainer.id,InicioFragment()
        )

    }
    override fun onStart() {
        super.onStart()

        initClass()
        Log.d("UCE",intent.getStringExtra("correo")!!)

        db.collection("users").document(intent.getStringExtra("correo")!!).get().addOnSuccessListener {
            usuarioItems.add(it.get("peso").toString())
            usuarioItems.add(it.get("genero").toString())
            usuarioItems.add(it.get("altura").toString())
            usuarioItems.add(it.get("edad").toString())
            Log.d("UCE",usuarioItems.toString())


        }

    }

    private fun initClass(){

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.inicio -> {
                    // Respond to navigation item 1 click
                    FragmentsManager().replaceFragment(
                        supportFragmentManager,binding.frmContainer.id,InicioFragment()
                    )
                    true
                }
                R.id.diario -> {
                    // Respond to navigation item 2 click
                    FragmentsManager().replaceFragment(
                        supportFragmentManager,binding.frmContainer.id,DiarioFragment()
                    )

                    true
                }
                R.id.informe -> {
                    // Respond to navigation item 2 click
                    FragmentsManager().replaceFragment(
                        supportFragmentManager,binding.frmContainer.id,InformeFragment()
                    )

                    true
                }
                else -> false

            }



        }
    }
}