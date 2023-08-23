package com.jimenez.ecuafit.ui.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI

import com.google.firebase.firestore.FirebaseFirestore
import com.jimenez.ecuafit.R
import com.jimenez.ecuafit.databinding.ActivityMenuBinding
import com.jimenez.ecuafit.ui.fragments.DiarioFragment
import com.jimenez.ecuafit.ui.fragments.InformeFragment
import com.jimenez.ecuafit.ui.fragments.InicioFragment
import com.jimenez.ecuafit.ui.utilities.FragmentsManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuBinding

    private lateinit var openAI: OpenAI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FragmentsManager().replaceFragment(
            supportFragmentManager,binding.frmContainer.id,InicioFragment()
        )
        openAI = OpenAI("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")

    }
    @OptIn(BetaOpenAI::class)
    override fun onStart() {
        super.onStart()

        initClass()
        val chatCompletionRequest = ChatCompletionRequest(
            model = ModelId("gpt-3.5-turbo"),
            messages = listOf(
                ChatMessage(
                    role = ChatRole.System,
                    content = "You are a helpful assistant!"
                ),
                ChatMessage(
                    role = ChatRole.User,
                    content = "Hello!"
                )
            )
        )
        lifecycleScope.launch(Dispatchers.Main){
            val completion: ChatCompletion = openAI.chatCompletion(chatCompletionRequest)
            Log.d("GPT", completion.toString())

        }

// or, as flow
        //val completions: kotlinx.coroutines.flow.Flow<ChatCompletionChunk> = openAI.chatCompletions(chatCompletionRequest)










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

}