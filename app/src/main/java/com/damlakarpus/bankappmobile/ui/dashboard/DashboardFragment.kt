package com.damlakarpus.bankappmobile.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.navigation.fragment.findNavController
import com.damlakarpus.bankappmobile.R
import com.damlakarpus.bankappmobile.databinding.FragmentDashboardBinding
import com.damlakarpus.bankappmobile.common.SessionManager

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DashboardViewModel by viewModels()
    private val accountAdapter = AccountAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Token yoksa login sayfasına dön
        if (SessionManager.token.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Lütfen giriş yapın", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.loginFragment)
            return
        }

        // RecyclerView setup
        binding.rvAccounts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = accountAdapter
        }

        // LiveData gözlemleme
        viewModel.accounts.observe(viewLifecycleOwner) { accounts ->
            accountAdapter.submitList(accounts)
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()

                // Token hatası veya hesap yoksa login sayfasına yönlendir
                if (it.contains("Token bulunamadı") || it.contains("Hesap bulunamadı")) {
                    SessionManager.clearSession()
                    findNavController().navigate(R.id.loginFragment)
                }
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Hesapları token ile yükle
        viewModel.loadAccounts()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
