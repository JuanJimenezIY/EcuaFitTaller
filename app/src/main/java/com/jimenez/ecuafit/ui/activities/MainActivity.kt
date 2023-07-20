package com.jimenez.ecuafit.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.jimenez.ecuafit.R
import com.jimenez.ecuafit.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
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


        binding.btnLogin.setOnClickListener {


            //Intents
            var intent = Intent(
                this, MenuActivity::class.java
            )

            startActivity(intent)
        }
    }}