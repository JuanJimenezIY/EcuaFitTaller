package com.jimenez.ecuafit.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jimenez.ecuafit.R
import com.jimenez.ecuafit.databinding.ActivityAguaBinding
import com.jimenez.ecuafit.databinding.ActivityPesoBinding
import com.jimenez.ecuafit.databinding.FragmentInformeBinding
import com.jimenez.ecuafit.ui.activities.AguaActivity
import com.jimenez.ecuafit.ui.activities.PesoActivity
import com.jimenez.ecuafit.ui.activities.RegistroActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [InformeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InformeFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private lateinit var binding: FragmentInformeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= FragmentInformeBinding.inflate(layoutInflater)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Configurar el click listener para el CardView de Peso
        binding.pesoText.setOnClickListener {
            val intent = Intent(requireContext(), PesoActivity::class.java)
            startActivity(intent)
        }
    }

}