package com.damlakarpus.bankappmobile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.damlakarpus.bankappmobile.base.Resource
import com.damlakarpus.bankappmobile.databinding.ActivityRegisterBinding
import com.damlakarpus.bankappmobile.viewmodel.RegisterViewModel
import com.damlakarpus.bankappmobile.model.register.RegisterRequest
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_BankAppMobile) // Tema
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        binding.btnRegister.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show()
            } else {
                val request = RegisterRequest(
                    userName = username,
                    email = email,
                    password = password
                )

                viewModel.registerUser(request).observe(this) { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is Resource.Success -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(
                                this,
                                "Kayıt başarılı: ${resource.data?.message ?: username}",
                                Toast.LENGTH_SHORT
                            ).show()
                            navigateToLogin()
                        }
                        is Resource.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this, "Hata: ${resource.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        binding.tvGoLogin.setOnClickListener {
            navigateToLogin()
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
