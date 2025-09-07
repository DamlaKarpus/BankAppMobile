package com.damlakarpus.bankappmobile.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.damlakarpus.bankappmobile.R
import com.damlakarpus.bankappmobile.databinding.FragmentDashboardBinding
import com.damlakarpus.bankappmobile.common.SessionManager
import com.damlakarpus.bankappmobile.viewmodel.TransactionViewModel

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DashboardViewModel by viewModels()
    private val transactionViewModel: TransactionViewModel by viewModels()

    private lateinit var transactionAdapter: TransactionAdapter

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

        // Kullanıcı bilgilerini göster (SessionManager’dan)
        binding.tvHello.text = "Hoşgeldin, ${SessionManager.userName ?: "Kullanıcı"}!"
        binding.tvIban.text = "IBAN: ${SessionManager.iban ?: "Yok"}"
        binding.tvBalance.text = "Bakiye: ${SessionManager.balance ?: 0.0} ₺"

        // RecyclerView setup (Son 3 işlem için)
        transactionAdapter = TransactionAdapter()
        binding.rvRecentTransactions.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = transactionAdapter
        }

        // Hesapları yükle
        viewModel.loadAccounts()

        // Para Gönder butonu
        binding.btnTransfer.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_transactionFragment)
        }

        // Tüm İşlemler butonu
        binding.btnAllTransactions.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_allTransactionsFragment)
        }

        // ✅ Chat butonu
        binding.btnChat.setOnClickListener {
            findNavController().navigate(R.id.chatFragment)
        }

        // LiveData gözlemleme
        setupObservers()
    }

    private fun setupObservers() {
        // Hesap bilgisi
        viewModel.accounts.observe(viewLifecycleOwner) { accounts ->
            accounts.firstOrNull()?.let { account ->
                binding.tvAccountName.text = "Vadesiz Hesap"
                binding.tvIban.text = "IBAN: ${account.iban}"
                binding.tvBalance.text = "Bakiye: ${account.balance} ₺"

                // Session güncelle
                SessionManager.iban = account.iban
                SessionManager.balance = account.balance

                // ✅ Hesap bilgisi geldikten sonra son 3 işlemi getir
                transactionViewModel.fetchRecentTransactions(account.iban)
            }
        }

        // Hata gözlemleri
        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                if (it.contains("Token bulunamadı") || it.contains("Hesap bulunamadı")) {
                    SessionManager.clearSession()
                    findNavController().navigate(R.id.loginFragment)
                }
            }
        }

        // Loading
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Son 3 işlem gözlemi
        transactionViewModel.recentTransactions.observe(viewLifecycleOwner) { transactions ->
            transactionAdapter.submitList(transactions)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
