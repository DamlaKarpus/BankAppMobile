package com.damlakarpus.bankappmobile.ui.register

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
                Toast.makeText(
                    requireContext(),
                    getString(R.string.register_fill_all),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val request = RegisterRequest(username, email, password)

            viewModel.registerUser(request).observe(viewLifecycleOwner) { resource ->
                when (resource) {
                    is Resource.Loading -> setLoading(true)

                    is Resource.Success -> {
                        setLoading(false)
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.register_success),
                            Toast.LENGTH_SHORT
                        ).show()
                        findNavController().navigate(
                            RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
                        )
                    }

                    is Resource.Error -> {
                        setLoading(false)
                        Toast.makeText(
                            requireContext(),
                            getString(
                                R.string.register_failed,
                                resource.message ?: getString(R.string.unknown_error)
                            ),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        binding.tvGoLogin.setOnClickListener {
            findNavController().navigate(
                RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
            )
        }
    }

    private fun setLoading(isLoading: Boolean) {
        // Lottie görünürlüğü
        binding.lottieLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
        if (isLoading) binding.lottieLoading.playAnimation() else binding.lottieLoading.pauseAnimation()

        // Formu kilitle
        binding.etUsername.isEnabled = !isLoading
        binding.etEmail.isEnabled = !isLoading
        binding.etPassword.isEnabled = !isLoading
        binding.btnRegister.isEnabled = !isLoading
        binding.tvGoLogin.isEnabled = !isLoading
    }
}
