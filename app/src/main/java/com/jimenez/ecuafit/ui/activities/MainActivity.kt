package com.jimenez.ecuafit.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jimenez.ecuafit.R
import com.jimenez.ecuafit.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val db= FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
    override fun onStart() {
        super.onStart()
        initClass()


    }
    override fun onDestroy() {
        super.onDestroy()
    }
    private fun initClass() {

        title="Autenticacion"

        binding.btnLogin.setOnClickListener {

            if(binding.txtName.text.isNotEmpty()&&binding.txtPassword.text.isNotEmpty()){
                Log.d("UCE",binding.txtContraseA.text.toString())
                Log.d("UCE",binding.txtCorreo.text.toString())
                db.collection("users").document(binding.txtName.text.toString()).get().addOnSuccessListener {
                    if(binding.txtPassword.text.toString()==it.get("contraseña")){
                        //Intents
                        var intent = Intent(
                            this, MenuActivity::class.java
                        )

                        startActivity(intent)
                    }
                    else{
                        Snackbar.make(binding.txtContraseA,"Usuario o contraseña incorrectas",Snackbar.LENGTH_SHORT)
                    }
                }
            }
            else{
                Snackbar.make(binding.txtContraseA,"Ingrese todos los campos",Snackbar.LENGTH_SHORT)

            }

        }

        binding.registro.setOnClickListener {


            //Intents
            var intent = Intent(
                this, RegistroActivity::class.java
            )

            startActivity(intent)
        }
    }}