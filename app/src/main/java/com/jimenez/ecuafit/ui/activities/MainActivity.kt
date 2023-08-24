package com.jimenez.ecuafit.ui.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
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
import com.jimenez.ecuafit.logic.UsuarioLogic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val db = FirebaseFirestore.getInstance()
    private val TAG = "UCE"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        validarSesion()
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

        binding.contrasenaOlvidada.setOnClickListener {
            autenticateBiometric()
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
                        message = "Resultado dudoso"
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
                        startActivity(Intent(this@MainActivity, MenuActivity::class.java))
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                    }
                })
            biometricManager.authenticate(biometricPrompt)
        } else {

            Snackbar.make(
                binding.contrasenaOlvidada,
                "No existen los requisitos necesarios",
                Snackbar.LENGTH_LONG
            )
        }
    }

    private fun checkBiometric(): Boolean {
        var returnValid: Boolean = false
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(
            BIOMETRIC_STRONG
        )) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                returnValid = true
            }

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                returnValid = false
            }

            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                returnValid = false
            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                val intentPrompt = Intent(Settings.ACTION_BIOMETRIC_ENROLL)
                intentPrompt.putExtra(
                    Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                    BIOMETRIC_STRONG or DEVICE_CREDENTIAL
                )
                startActivity(intentPrompt)
                returnValid = false
            }
        }
        return returnValid
    }

    @SuppressLint("SuspiciousIndentation")
    private fun logIn(email: String, password: String) {
        val con=db.collection("users").document(binding.txtName.text.toString()).get()
            con.addOnSuccessListener {

                if(con.result.data!=null){
                    if (comprobar(binding.txtPassword.text.toString(), it.getString("contraseña"))) {
                        guardarSesion()
                        lifecycleScope.launch (Dispatchers.Main){
                            withContext(Dispatchers.IO){
                                UsuarioLogic().recuperarUsuario(binding.txtName.text.toString())

                            }
                        }

                        //Intents
                        var intent = Intent(
                            this, MenuActivity::class.java
                        )

                        startActivity(intent)

                    }
                    else {


                        Snackbar.make(binding.txtContraseA,"Usuario o contraseña incorrectas",Snackbar.LENGTH_SHORT).show()
                    }
                }
                else{
                    Snackbar.make(binding.txtContraseA,"Usuario no existe",Snackbar.LENGTH_SHORT).show()
                }

            }

    }
    private fun guardarSesion(){
        val sharedPref=getSharedPreferences("sesion",Context.MODE_PRIVATE)
        val estado=true;
        with(sharedPref.edit()){
            putBoolean("estado_usu",estado)
                .apply()
        }


    }
    private fun validarSesion(){
        val sharedPref=getSharedPreferences("sesion",Context.MODE_PRIVATE)
        if(sharedPref.getBoolean("estado_usu",false)!=false){
            var intent = Intent(
                this, MenuActivity::class.java
            )
            startActivity(intent)

    }
}
    private fun comprobar(pass: String, hash: String?): Boolean {
        val hashBytes = hash?.toByteArray() ?: return false
        var result = BCrypt.verifyer().verify(pass.toCharArray(), hashBytes).verified
        return result


    }

}

