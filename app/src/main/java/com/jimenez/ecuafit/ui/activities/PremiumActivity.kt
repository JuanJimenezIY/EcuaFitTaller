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
            token = "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", timeout =
            Timeout(socket = 60.seconds)
        )
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch(Dispatchers.Main){
            var user= withContext(Dispatchers.IO){
               EcuaFit.getDbUsuarioInstance().usuarioDao().getAll()
            }
            generaReporte(user)

        }
    }

    @OptIn(BetaOpenAI::class)
    suspend fun generaReporte(usuarioDB: UsuarioDB) {
        val chatCompletionRequest = ChatCompletionRequest(
            model = ModelId("gpt-3.5-turbo"),
            messages = listOf(

                ChatMessage(
                    role = ChatRole.Assistant,
                    content = "Damer recomedaciones bien detalldas dieta y ejercicios especificos para mejorar mi estado fisico actualmente mido "
                            + usuarioDB.altura + ",peso "+usuarioDB.peso[0]+",soy de genero "+usuarioDB.genero+" y tengo "+usuarioDB.edad+
                            " años de edad calcula mi requerimiento calorico diario y dame recomendaciones"
                )
            )
        )
        val completion: ChatCompletion = openAI.chatCompletion((chatCompletionRequest))
        Log.d("UCE",completion.choices.toString())
    }
}