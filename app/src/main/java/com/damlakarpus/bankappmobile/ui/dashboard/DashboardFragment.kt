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
import java.text.NumberFormat
import java.util.*

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
            Toast.makeText(requireContext(), getString(R.string.please_login), Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.loginFragment)
            return
        }

        // Kullanıcı bilgilerini göster (SessionManager’dan)
        val formattedBalance = NumberFormat.getCurrencyInstance(Locale("tr", "TR"))
            .format(SessionManager.balance ?: 0.0)

        binding.tvHello.text =
            getString(R.string.hello_user, SessionManager.userName ?: getString(R.string.app_name))
        binding.tvIban.text =
            getString(R.string.iban, SessionManager.iban ?: getString(R.string.chat_no_iban))
        binding.tvBalance.text = getString(R.string.balance, formattedBalance)

        // RecyclerView setup (Son 3 işlem için)
        transactionAdapter = TransactionAdapter(currentIban = SessionManager.iban.orEmpty())
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

        // Chat butonu
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
                val formattedBalance = NumberFormat.getCurrencyInstance(Locale("tr", "TR"))
                    .format(account.balance)

                binding.tvAccountName.text = getString(R.string.default_account_name)
                binding.tvIban.text = getString(R.string.iban, account.iban)
                binding.tvBalance.text = getString(R.string.balance, formattedBalance)

                // Session güncelle
                SessionManager.iban = account.iban
                SessionManager.balance = account.balance

                // Hesap bilgisi geldikten sonra son 3 işlemi getir
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
