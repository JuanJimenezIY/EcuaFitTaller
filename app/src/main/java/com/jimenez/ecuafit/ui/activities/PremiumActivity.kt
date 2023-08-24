package com.jimenez.ecuafit.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.http.Timeout
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.LoggingConfig
import com.aallam.openai.client.OpenAI
import com.jimenez.ecuafit.R
import com.jimenez.ecuafit.data.entities.UsuarioDB
import com.jimenez.ecuafit.databinding.ActivityPremiumBinding
import com.jimenez.ecuafit.logic.UsuarioLogicDB
import com.jimenez.ecuafit.ui.utilities.EcuaFit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.seconds

class PremiumActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPremiumBinding
    private lateinit var openAI: OpenAI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPremiumBinding.inflate(layoutInflater)
        setContentView(binding.root)

        openAI = OpenAI(
            "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", LoggingConfig(),Timeout(socket = 120.seconds)
        )
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch(Dispatchers.Main) {
            var user = withContext(Dispatchers.IO) {
                EcuaFit.getDbUsuarioInstance().usuarioDao().getAll()
            }
            generaReporte(user)

        }
    }

    fun msg() {

    }

    @OptIn(BetaOpenAI::class)
    suspend fun generaReporte(usuarioDB: UsuarioDB) {
        val chatCompletionRequest = ChatCompletionRequest(
            model = ModelId("gpt-3.5-turbo"),
            messages = listOf(

                ChatMessage(
                    role = ChatRole.Assistant,
                    content = "Damer recomedaciones bien detalldas dieta y ejercicios especificos para mejorar mi estado fisico actualmente mido "
                            + usuarioDB.altura + ",peso " + usuarioDB.peso[0] + ",soy de genero " + usuarioDB.genero + " y tengo " + usuarioDB.edad +
                            " años de edad calcula mi requerimiento calorico diario con nivel de actividad fisica baja y dame recomendaciones"
                )
            )
        )
        val completion: ChatCompletion = openAI.chatCompletion((chatCompletionRequest))
        Log.d("UCE", completion.choices.toString())
    }
}