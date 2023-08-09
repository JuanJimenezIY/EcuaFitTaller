package com.jimenez.ecuafit.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.jimenez.ecuafit.R
import com.google.firebase.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.jimenez.ecuafit.databinding.ActivityRegistroBinding

class RegistroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistroBinding
    private lateinit var auth: FirebaseAuth
    private val TAG = "UCE"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        auth = Firebase.auth
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        binding.buttonRegister.setOnClickListener {
            register(
                binding.editTextEmail.text.toString(),
                binding.editTextContraseA.text.toString()
            )
        }
    }

    fun register(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    Toast.makeText(
                        baseContext,
                        "Register succesfull.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    val user = auth.currentUser
                    //  updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    //  updateUI(null)
                }
            }
    }

    fun disableAll() {
        binding.buttonRegister.isEnabled = false
    }
}