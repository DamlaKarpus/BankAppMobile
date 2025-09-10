package com.damlakarpus.bankappmobile.ui.dashboard

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.damlakarpus.bankappmobile.R
import com.damlakarpus.bankappmobile.common.SessionManager
import com.damlakarpus.bankappmobile.databinding.FragmentDashboardBinding
import com.damlakarpus.bankappmobile.viewmodel.TransactionViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.NumberFormat
import java.util.Locale

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

        // Donanım geri tuşu: çıkış diyaloğu
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() = showExitDialog()
            }
        )

        // 🚨 Token yoksa login sayfasına yönlendir
        if (SessionManager.token.isNullOrEmpty()) {
            SessionManager.clearSession()
            Toast.makeText(requireContext(), getString(R.string.please_login), Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.loginFragment)
            return
        }

        // Kullanıcı bilgileri ekrana bas
        val formattedBalance = NumberFormat.getCurrencyInstance(Locale("tr", "TR"))
            .format(SessionManager.balance ?: 0.0)

        binding.tvHello.text = getString(
            R.string.hello_user,
            SessionManager.userName ?: getString(R.string.app_name)
        )
        binding.tvIban.text = getString(
            R.string.iban,
            SessionManager.iban ?: getString(R.string.chat_no_iban)
        )
        binding.tvBalance.text = getString(R.string.balance, formattedBalance)

        // IBAN kopyala
        binding.btnCopyIban?.setOnClickListener {
            val iban = SessionManager.iban
            if (iban.isNullOrBlank()) {
                Toast.makeText(requireContext(), getString(R.string.chat_no_iban), Toast.LENGTH_SHORT).show()
            } else {
                val cm = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                cm.setPrimaryClip(ClipData.newPlainText("IBAN", iban))
                Toast.makeText(requireContext(), getString(R.string.copy_iban_done), Toast.LENGTH_SHORT).show()
            }
        }

        // Son 3 işlem listesi
        transactionAdapter = TransactionAdapter(currentIban = SessionManager.iban.orEmpty())
        binding.rvRecentTransactions.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = transactionAdapter
        }

        // Hesapları yükle
        viewModel.loadAccounts()

        // Para Gönder
        binding.btnTransfer.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_transactionFragment)
        }

        // Tüm İşlemler
        binding.btnAllTransactions.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_allTransactionsFragment)
        }

        // Chat
        binding.btnChat.setOnClickListener {
            findNavController().navigate(R.id.chatFragment)
        }

        // LiveData gözlemleri
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

                // Son 3 işlem
                transactionViewModel.fetchRecentTransactions(account.iban)
            }
        }

        // Hata yakala
        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                if (it.contains("Token") || it.contains("Hesap")) {
                    SessionManager.clearSession()
                    findNavController().navigate(R.id.loginFragment)
                }
            }
        }

        // Loading
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Son 3 işlem listesi
        transactionViewModel.recentTransactions.observe(viewLifecycleOwner) { transactions ->
            transactionAdapter.submitList(transactions)
        }
    }

    private fun showExitDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.exit_title))
            .setMessage(getString(R.string.exit_message))
            .setPositiveButton(getString(R.string.exit_yes)) { _, _ ->
                // ✅ Çıkış → oturum kapat ve login ekranına dön
                SessionManager.clearSession()
                val navOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.dashboardFragment, true) // Dashboard dahil stack temizlensin
                    .build()
                findNavController().navigate(R.id.loginFragment, null, navOptions)
            }
            .setNegativeButton(getString(R.string.exit_no), null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
