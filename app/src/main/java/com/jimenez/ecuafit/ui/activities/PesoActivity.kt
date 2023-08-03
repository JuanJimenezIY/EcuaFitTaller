package com.jimenez.ecuafit.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jimenez.ecuafit.R
import com.jimenez.ecuafit.databinding.ActivityAguaBinding
import com.jimenez.ecuafit.databinding.ActivityPesoBinding

class PesoActivity : AppCompatActivity() {
    private lateinit var binding:ActivityPesoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPesoBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}