package com.damlakarpus.bankappmobile.ui.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.damlakarpus.bankappmobile.common.SessionManager
import com.damlakarpus.bankappmobile.data.model.TransactionRequest
import com.damlakarpus.bankappmobile.data.model.transaction.TransactionResponse
import com.damlakarpus.bankappmobile.databinding.FragmentTransactionBinding
import com.damlakarpus.bankappmobile.viewmodel.TransactionViewModel

class TransactionFragment : Fragment() {

    private var _binding: FragmentTransactionBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TransactionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnTransfer.setOnClickListener {
            val amountText = binding.etAmount.text.toString().trim()
            val targetIban = binding.etTargetIban.text.toString().trim()

            if (amountText.isNotEmpty() && targetIban.isNotEmpty() && !SessionManager.iban.isNullOrEmpty()) {
                val amountValue = try {
                    amountText.toBigDecimal()
                } catch (e: NumberFormatException) {
                    null
                }

                if (amountValue != null) {
                    val request = TransactionRequest(
                        accountIban = SessionManager.iban!!,
                        targetAccountIban = targetIban,
                        amount = amountValue
                    )
                    viewModel.transfer(request)
                } else {
                    Toast.makeText(requireContext(), "Geçerli bir tutar girin", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show()
            }
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.transactionResult.observe(viewLifecycleOwner) { response: TransactionResponse? ->
            response?.let {
                if (it.success == true && it.transaction != null) {
                    val transaction = it.transaction
                    Toast.makeText(
                        requireContext(),
                        "İşlem başarılı!\nGönderilen IBAN: ${transaction.targetAccountIban}\nTutar: ${transaction.amount}",
                        Toast.LENGTH_LONG
                    ).show()
                    findNavController().popBackStack()
                } else {
                    Toast.makeText(
                        requireContext(),
                        it.message ?: "Hata oluştu",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMsg ->
            errorMsg?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
