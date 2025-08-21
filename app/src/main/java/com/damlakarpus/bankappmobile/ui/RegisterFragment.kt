package com.damlakarpus.bankappmobile.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.damlakarpus.bankappmobile.base.BaseFragment
import com.damlakarpus.bankappmobile.databinding.FragmentRegisterBinding
import com.damlakarpus.bankappmobile.model.register.RegisterRequest
import com.damlakarpus.bankappmobile.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {

    private val viewModel: RegisterViewModel by viewModels()

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentRegisterBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show()
            } else {
                val request = RegisterRequest(username, email, password)
                observeResource(viewModel.registerUser(request), viewLifecycleOwner) { data ->
                    Toast.makeText(
                        requireContext(),
                        "Kayıt başarılı: ${data.message ?: username}",
                        Toast.LENGTH_SHORT
                    ).show()
                    // LoginFragment’e geçiş
                    findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
                }
            }
        }

        binding.tvGoLogin.setOnClickListener {
            findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
        }
    }
}
