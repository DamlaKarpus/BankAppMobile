package com.damlakarpus.bankappmobile.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.damlakarpus.bankappmobile.base.BaseFragment
import com.damlakarpus.bankappmobile.base.Resource
import com.damlakarpus.bankappmobile.data.model.register.RegisterRequest
import com.damlakarpus.bankappmobile.databinding.FragmentRegisterBinding
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
                return@setOnClickListener
            }

            val request = RegisterRequest(username, email, password)

            // LiveData gözlemle
            viewModel.registerUser(request).observe(viewLifecycleOwner) { resource ->

                when (resource) {
                    is Resource.Success -> {
                        Toast.makeText(requireContext(), "Kayıt başarılı", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
                    }
                    is Resource.Error -> {
                        Toast.makeText(requireContext(), "Kayıt başarısız: ${resource.message ?: "Bilinmeyen hata"}", Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Loading -> {
                        // İstersen progress bar gösterebilirsin
                    }
                }
            }
        }

        binding.tvGoLogin.setOnClickListener {
            findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
        }
    }
}
