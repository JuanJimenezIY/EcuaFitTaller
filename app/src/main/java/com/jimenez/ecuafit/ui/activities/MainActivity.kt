package com.jimenez.ecuafit.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.jimenez.ecuafit.R
import com.jimenez.ecuafit.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var auth: FirebaseAuth
    private val TAG = "UCE"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        // Initialize Firebase Auth
        auth = Firebase.auth
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
//        val currentUser = auth.currentUser
//        if (currentUser != null) {
//            reload()
//        }

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
                logIn(binding.txtName.text.toString(),binding.txtPassword.text.toString())
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
    private fun logIn(email:String,password:String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    Toast.makeText(
                        baseContext,
                        "Authentication succesfull.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    val user = auth.currentUser
                    //updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                  //  updateUI(null)
                }
            }
    }
}