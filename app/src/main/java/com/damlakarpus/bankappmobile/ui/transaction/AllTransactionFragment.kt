package com.damlakarpus.bankappmobile.ui.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.damlakarpus.bankappmobile.R
import com.damlakarpus.bankappmobile.common.SessionManager
import com.damlakarpus.bankappmobile.databinding.FragmentAllTransactionsBinding
import com.damlakarpus.bankappmobile.ui.dashboard.TransactionAdapter
import com.damlakarpus.bankappmobile.viewmodel.TransactionViewModel
import com.google.android.material.appbar.MaterialToolbar

class AllTransactionsFragment : Fragment() {

    private var _binding: FragmentAllTransactionsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TransactionViewModel by viewModels()
    private lateinit var transactionAdapter: TransactionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllTransactionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ✅ Toolbar başlığını burada sıfırla (boş bırak)
        val toolbar = requireActivity().findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.title = ""

        // RecyclerView setup -> kendi IBAN’ı TransactionAdapter’a geçiyoruz
        transactionAdapter = TransactionAdapter(currentIban = SessionManager.iban.orEmpty())
        binding.rvAllTransactions.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = transactionAdapter
        }

        // Eğer IBAN null veya boşsa uyarı verelim
        val currentIban = SessionManager.iban
        if (currentIban.isNullOrEmpty()) {
            Toast.makeText(requireContext(), getString(R.string.chat_no_iban), Toast.LENGTH_SHORT).show()
            return
        }

        // Tüm işlemleri getir
        viewModel.fetchAllTransactions(currentIban)

        // Observer
        viewModel.allTransactions.observe(viewLifecycleOwner) { transactions ->
            transactionAdapter.submitList(transactions)
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Çıkışta da başlık boş kalsın
        val toolbar = requireActivity().findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.title = ""
        _binding = null
    }
}
