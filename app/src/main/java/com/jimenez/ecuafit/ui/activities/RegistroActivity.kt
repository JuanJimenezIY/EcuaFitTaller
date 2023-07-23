package com.jimenez.ecuafit.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import com.jimenez.ecuafit.R
import com.google.firebase.*
import com.google.firebase.firestore.FirebaseFirestore
import com.jimenez.ecuafit.databinding.ActivityRegistroBinding

class RegistroActivity : AppCompatActivity() {
    private lateinit var binding:ActivityRegistroBinding
    private val db=FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        binding.buttonRegister.setOnClickListener {
            Log.d("UCE","SAVE")
            val res=db.collection("users").document(binding.editTextEmail.text.toString()).set(
                hashMapOf("altura" to binding.editTextAltura.text.toString(),
                "correo" to binding.editTextEmail.text.toString(),
                "edad" to binding.editTextEdad.text.toString(),
                "genero" to binding.editTextGenero.text.toString(),
                "nombre" to binding.editTextName.text.toString(),
                "peso" to binding.editTextPeso.text.toString(),
                "contraseña" to binding.editTextContraseA.text.toString())
            )
            res.addOnCompleteListener {
                if (it.isSuccessful){
                    Snackbar.make(binding.textViewEmailLabel,"Registro completo",Snackbar.LENGTH_SHORT).show()
                }
            }


        }
    }
    fun register(){

    }
}