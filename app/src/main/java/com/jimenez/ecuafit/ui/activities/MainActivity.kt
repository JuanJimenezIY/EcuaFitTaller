package com.jimenez.ecuafit.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
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
    private val db = FirebaseFirestore.getInstance()
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

    @SuppressLint("ResourceAsColor")
    private fun initClass() {

        binding.huellaDactilar.setOnClickListener{
            autenticateBiometric()
        }

        binding.btnLogin.setOnClickListener {

            if (binding.txtName.text.isNotEmpty() && binding.txtPassword.text.isNotEmpty()) {
                binding.btnLogin.isEnabled=false
                //binding.btnLogin.setBackgroundColor(R.color.black)
                db.collection("users").document(binding.txtName.text.toString()).get()
                    .addOnSuccessListener {
                        if (binding.txtPassword.text.toString() == it.get("contraseña")) {
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
                    }
            } else {
                Snackbar.make(
                    binding.txtContraseA,
                    "Ingrese todos los campos",
                    Snackbar.LENGTH_SHORT
                ).show()

            }
           // binding.btnLogin.setBackgroundColor(R.color.boton_color)
            binding.btnLogin.isEnabled=true
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
                        message="Resultado dudoso"
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
    private fun autenticateBiometric() {
        if (checkBiometric()) {
            val executor = ContextCompat.getMainExecutor(this)
            val biometricPrompt = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Autenticacion requerida")
                .setSubtitle("Coloque su huella digital")
                .setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
                // .setNegativeButtonText("Cancelar")
                .build()
            val biometricManager = BiometricPrompt(
                this,
                mainExecutor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                    }

                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        startActivity(Intent(this@MainActivity,MenuActivity::class.java))
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                    }
                })
            biometricManager.authenticate(biometricPrompt)
        } else {

            Snackbar.make(binding.huellaDactilar,"No existen los requisitos necesarios",Snackbar.LENGTH_LONG)
        }
    }
    private fun checkBiometric() : Boolean{
        var returnValid: Boolean=false
        val biometricManager = BiometricManager.from(this)
        when(biometricManager.canAuthenticate(
            BIOMETRIC_STRONG
        )){
            BiometricManager.BIOMETRIC_SUCCESS->{
                returnValid= true
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE->{
                returnValid= false
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE->{
                returnValid= false
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED->{
                val intentPrompt = Intent(Settings.ACTION_BIOMETRIC_ENROLL)
                intentPrompt.putExtra(
                    Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                    BIOMETRIC_STRONG or DEVICE_CREDENTIAL
                )
                startActivity(intentPrompt)
                returnValid= false
            }
        }
        return returnValid
    }
}