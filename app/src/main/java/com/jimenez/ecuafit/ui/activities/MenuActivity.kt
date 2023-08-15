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
    private var usuarioItems: MutableList<String> = mutableListOf();
    private val db = FirebaseFirestore.getInstance()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FragmentsManager().replaceFragment(
            supportFragmentManager,binding.frmContainer.id,InicioFragment()
        )

    }
    override fun onStart() {
        super.onStart()
        Log.d("UCE", intent.dataString.toString())

        db.collection("users").document(intent.getStringExtra("correo")!!).get().addOnSuccessListener {
            usuarioItems.add(it.get("peso").toString().replace("[","").replace("]",""))
            usuarioItems.add(it.get("genero").toString())
            usuarioItems.add(it.get("altura").toString())
            usuarioItems.add(it.get("edad").toString())



        }.addOnCompleteListener {
            Log.d("UCE",usuarioItems.toString())
            var im=calcular(usuarioItems[1],usuarioItems[0],usuarioItems[2],usuarioItems[3])
            Log.d("UCE",im.toString())

        }


        initClass()




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
    private fun calcular(genero:String,estatura:String,peso:String,edad:String):Double{
        Log.d("UCE",estatura)
        Log.d("UCE",edad)

        if(genero.contains("mas")){
            return 66.573+((13.751*peso.toDouble())+(5.0033*estatura.toDouble())-(6.55*edad.toDouble()))
        }
        else{
            return 0.1
        }
    }
}