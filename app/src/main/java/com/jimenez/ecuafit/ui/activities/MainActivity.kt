package com.jimenez.ecuafit.ui.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import at.favre.lib.crypto.bcrypt.BCrypt
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.jimenez.ecuafit.databinding.ActivityMainBinding
import com.jimenez.ecuafit.logic.UsuarioLogicFire
import com.jimenez.ecuafit.ui.utilities.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val db = FirebaseFirestore.getInstance()
    private val TAG = "UCE"
    private lateinit var sharedPref: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = getSharedPreferences("sesion", Context.MODE_PRIVATE)

    }

    override fun onStart() {
        super.onStart()

        initClass()
    }

    @SuppressLint("ResourceAsColor")
    private fun initClass() {

        if (SessionManager.validarSesion(sharedPref)) {
            var intent = Intent(
                this, MenuActivity::class.java
            )
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {

            if (binding.txtName.text.isNotEmpty() && binding.txtPassword.text.isNotEmpty()) {
                logIn(binding.txtName.text.toString(), binding.txtPassword.text.toString())
            } else {
                Snackbar.make(
                    binding.txtContraseA,
                    "Ingrese todos los campos",
                    Snackbar.LENGTH_SHORT
                ).show()

            }
            // binding.btnLogin.setBackgroundColor(R.color.boton_color)
            binding.btnLogin.isEnabled = true
        }
        val appResultLocal =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultActivity ->
                val sn = Snackbar.make(binding.imageView, "", Snackbar.LENGTH_SHORT)
                var message = ""
                when (resultActivity.resultCode) {
                    RESULT_OK -> {
                        message =
                            resultActivity.data?.getStringExtra("result").orEmpty()
                    }

                    RESULT_CANCELED -> {

                        message =
                            "Error en el registro"
                    }

                    else -> {
                        message = "Registro dudoso"
                    }
                }
                sn.setText(message.toString())
                sn.show()
            }
        binding.registro.setOnClickListener {


            //Intents
            var intent = Intent(
                this, RegistroActivity::class.java
            )

            appResultLocal.launch(intent)
        }
    }




    @SuppressLint("SuspiciousIndentation")
    private fun logIn(email: String, password: String) {
        val con = db.collection("users").document(email).get()
        con.addOnSuccessListener {

            if (con.result.data != null) {
                if (SessionManager.comprobar(password, it.getString("contraseña"))) {
                    SessionManager.guardarSesion(sharedPref)
                    lifecycleScope.launch(Dispatchers.Main) {
                        withContext(Dispatchers.IO) {
                            UsuarioLogicFire().recuperarUsuario(email)

                        }
                    }

                    //Intents
                    var intent = Intent(
                        this, MenuActivity::class.java
                    )

                    startActivity(intent)

                } else {


                    Snackbar.make(
                        binding.txtContraseA,
                        "Usuario o contraseña incorrectas",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            } else {
                Snackbar.make(binding.txtContraseA, "Usuario no existe", Snackbar.LENGTH_SHORT)
                    .show()
            }

        }

    }

}




