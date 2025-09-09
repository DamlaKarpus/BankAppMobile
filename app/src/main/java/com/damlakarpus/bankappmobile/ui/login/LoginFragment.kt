package com.damlakarpus.bankappmobile.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.damlakarpus.bankappmobile.R
import com.damlakarpus.bankappmobile.base.BaseFragment
import com.damlakarpus.bankappmobile.base.Resource
import com.damlakarpus.bankappmobile.common.SessionManager
import com.damlakarpus.bankappmobile.data.model.login.LoginRequest
import com.damlakarpus.bankappmobile.databinding.FragmentLoginBinding
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
                Toast.makeText(
                    requireContext(),
                    getString(R.string.login_fill_all_fields),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                performLogin(email, password)
            }
        }

        binding.tvRegister.setOnClickListener {
            findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            )
        }
    }

    private fun setLoading(isLoading: Boolean) {
        // Lottie’yi göster/gizle
        binding.lottieLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
        if (isLoading) binding.lottieLoading.playAnimation() else binding.lottieLoading.pauseAnimation()

        // Formu kilitle/aç
        binding.etEmail.isEnabled = !isLoading
        binding.etPassword.isEnabled = !isLoading
        binding.btnLogin.isEnabled = !isLoading
        binding.tvRegister.isEnabled = !isLoading
    }

    private fun performLogin(email: String, password: String) {
        val request = LoginRequest(email, password)

        viewModel.loginUser(request).observe(viewLifecycleOwner) { res ->
            when (res) {
                is Resource.Loading -> setLoading(true)

                is Resource.Success -> {
                    setLoading(false)

                    // Token’ı SessionManager’a kaydet
                    val token = res.data?.token
                    if (!token.isNullOrEmpty()) {
                        SessionManager.token = token
                    }

                    Toast.makeText(
                        requireContext(),
                        getString(R.string.login_success),
                        Toast.LENGTH_SHORT
                    ).show()

                    // Dashboard’a geç
                    findNavController().navigate(
                        LoginFragmentDirections.actionLoginFragmentToDashboardFragment()
                    )
                }

                is Resource.Error -> {
                    setLoading(false)
                    Toast.makeText(
                        requireContext(),
                        res.message ?: getString(R.string.unknown_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
