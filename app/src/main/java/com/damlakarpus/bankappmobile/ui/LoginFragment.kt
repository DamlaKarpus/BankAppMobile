package com.damlakarpus.bankappmobile.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.damlakarpus.bankappmobile.base.BaseFragment
import com.damlakarpus.bankappmobile.databinding.FragmentLoginBinding
import com.damlakarpus.bankappmobile.model.login.LoginRequest
import com.damlakarpus.bankappmobile.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private val viewModel: LoginViewModel by viewModels()

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentLoginBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show()
            } else {
                performLogin(email, password)
            }
        }

        binding.tvRegister.setOnClickListener {
            // Navigation Component ile RegisterFragment'e geçiş
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }
    }

    private fun performLogin(email: String, password: String) {
        val request = LoginRequest(email, password)

        // BaseFragment içindeki observeResource kullanımı
        observeResource(viewModel.loginUser(request), viewLifecycleOwner) { data ->
            Toast.makeText(requireContext(), "Login başarılı!", Toast.LENGTH_SHORT).show()
            // DashboardFragment veya başka fragment'e geçiş
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToDashboardFragment())
        }
    }
}
