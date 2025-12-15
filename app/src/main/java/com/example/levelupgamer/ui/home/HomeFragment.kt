package com.example.levelupgamer.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.levelupgamer.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        textView.text = "¡Bienvenido a LevelUpGamer!\n\n" +
                "🎮 Tu tienda gaming de confianza\n\n" +
                "Explora nuestro catálogo de productos gaming:\n" +
                "• Mouses gaming de alta precisión\n" +
                "• Teclados mecánicos RGB\n" +
                "• Audífonos con sonido envolvente\n" +
                "• Monitores de alta frecuencia\n" +
                "• Y mucho más...\n\n" +
                "Navega por el menú para gestionar productos."

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}