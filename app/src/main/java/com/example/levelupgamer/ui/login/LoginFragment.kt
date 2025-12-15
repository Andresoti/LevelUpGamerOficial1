package com.example.levelupgamer.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.levelupgamer.databinding.FragmentLoginBinding
import com.example.levelupgamer.models.UserProfile
import com.example.levelupgamer.models.Validator
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private var isLoginMode = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupListeners()
    }

    private fun setupUI() {
        updateUIMode()
    }

    private fun setupListeners() {
        binding.btnSubmit.setOnClickListener {
            if (isLoginMode) {
                performLogin()
            } else {
                performRegister()
            }
        }

        binding.tvToggleMode.setOnClickListener {
            isLoginMode = !isLoginMode
            updateUIMode()
        }
    }

    private fun updateUIMode() {
        if (isLoginMode) {
            binding.btnSubmit.text = "Iniciar Sesión"
            binding.tvToggleMode.text = "¿No tienes cuenta? Regístrate"
            binding.layoutRegisterFields.visibility = View.GONE
        } else {
            binding.btnSubmit.text = "Registrarse"
            binding.tvToggleMode.text = "¿Ya tienes cuenta? Inicia sesión"
            binding.layoutRegisterFields.visibility = View.VISIBLE
        }
    }

    private fun performLogin() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()

        if (!validateLoginInputs(email, password)) {
            return
        }

        lifecycleScope.launch {
            try {
                Toast.makeText(
                    requireContext(),
                    "✓ Login exitoso para: $email",
                    Toast.LENGTH_SHORT
                ).show()

                clearFields()
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "Error: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun performRegister() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()
        val fullName = binding.etFullName.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val address = binding.etAddress.text.toString().trim()
        val city = binding.etCity.text.toString().trim()
        val postalCode = binding.etPostalCode.text.toString().trim()
        val country = binding.etCountry.text.toString().trim()

        if (!validateRegisterInputs(
                email, password, fullName, phone,
                address, city, postalCode, country
            )) {
            return
        }

        lifecycleScope.launch {
            try {
                val newUser = UserProfile(
                    email = email,
                    password = password,
                    fullName = fullName,
                    phone = phone,
                    address = address,
                    city = city,
                    postalCode = postalCode,
                    country = country
                )

                Toast.makeText(
                    requireContext(),
                    "✓ Registro exitoso: $fullName",
                    Toast.LENGTH_SHORT
                ).show()

                clearFields()
                isLoginMode = true
                updateUIMode()
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "Error: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun validateLoginInputs(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            binding.etEmail.error = "El email es requerido"
            return false
        }

        if (!Validator.isValidEmail(email)) {
            binding.etEmail.error = "Email inválido"
            return false
        }

        if (password.isEmpty()) {
            binding.etPassword.error = "La contraseña es requerida"
            return false
        }

        if (!Validator.isValidPassword(password)) {
            binding.etPassword.error = "La contraseña debe tener al menos 6 caracteres"
            return false
        }

        return true
    }

    private fun validateRegisterInputs(
        email: String, password: String, fullName: String,
        phone: String, address: String, city: String,
        postalCode: String, country: String
    ): Boolean {

        if (!validateLoginInputs(email, password)) {
            return false
        }

        if (fullName.isEmpty()) {
            binding.etFullName.error = "El nombre completo es requerido"
            return false
        }

        if (phone.isEmpty()) {
            binding.etPhone.error = "El teléfono es requerido"
            return false
        }

        if (!Validator.isValidPhone(phone)) {
            binding.etPhone.error = "Teléfono inválido"
            return false
        }

        if (address.isEmpty()) {
            binding.etAddress.error = "La dirección es requerida"
            return false
        }

        if (city.isEmpty()) {
            binding.etCity.error = "La ciudad es requerida"
            return false
        }

        if (postalCode.isEmpty()) {
            binding.etPostalCode.error = "El código postal es requerido"
            return false
        }

        if (!Validator.isValidPostalCode(postalCode)) {
            binding.etPostalCode.error = "Código postal inválido"
            return false
        }

        if (country.isEmpty()) {
            binding.etCountry.error = "El país es requerido"
            return false
        }

        return true
    }

    private fun clearFields() {
        binding.etEmail.text?.clear()
        binding.etPassword.text?.clear()
        binding.etFullName.text?.clear()
        binding.etPhone.text?.clear()
        binding.etAddress.text?.clear()
        binding.etCity.text?.clear()
        binding.etPostalCode.text?.clear()
        binding.etCountry.text?.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}