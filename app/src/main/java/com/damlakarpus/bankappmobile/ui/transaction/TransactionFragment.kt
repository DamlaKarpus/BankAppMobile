package com.damlakarpus.bankappmobile.ui.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieAnimationView
import com.damlakarpus.bankappmobile.R
import com.damlakarpus.bankappmobile.common.SessionManager
import com.damlakarpus.bankappmobile.data.model.TransactionRequest
import com.damlakarpus.bankappmobile.data.model.transaction.TransactionResponse
import com.damlakarpus.bankappmobile.databinding.FragmentTransactionBinding
import com.damlakarpus.bankappmobile.viewmodel.TransactionViewModel
import com.google.android.material.appbar.MaterialToolbar

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

        // ✅ Toolbar başlığını ayarla (sadece TransactionFragment için)
        val toolbar = requireActivity().findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.title = getString(R.string.app_name)
        toolbar.setTitleTextColor(ContextCompat.getColor(requireContext(), R.color.primaryDark))

        binding.btnTransfer.setOnClickListener {
            val amountText = binding.etAmount.text.toString().trim()
            val targetIban = binding.etTargetIban.text.toString().trim()

            if (amountText.isNotEmpty() && targetIban.isNotEmpty() && !SessionManager.iban.isNullOrEmpty()) {
                val amountValue = amountText.toBigDecimalOrNull()
                if (amountValue != null) {
                    val request = TransactionRequest(
                        accountIban = SessionManager.iban!!,
                        targetAccountIban = targetIban,
                        amount = amountValue
                    )
                    viewModel.transfer(request)
                } else {
                    Toast.makeText(requireContext(), getString(R.string.error_invalid_amount), Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), getString(R.string.error_fill_all_fields), Toast.LENGTH_SHORT).show()
            }
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.transactionResult.observe(viewLifecycleOwner) { response: TransactionResponse? ->
            response?.let {
                if (it.success == true && it.transaction != null) {
                    // Formu gizle, success animasyonunu göster
                    setFormEnabled(false)
                    binding.progressBar.visibility = View.GONE
                    showSuccessAndExit()
                } else {
                    Toast.makeText(requireContext(), it.message ?: getString(R.string.unknown_error), Toast.LENGTH_SHORT).show()
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
            binding.btnTransfer.isEnabled = !isLoading
        }
    }

    private fun showSuccessAndExit() {
        val lottie: LottieAnimationView = binding.lottieSuccess
        lottie.visibility = View.VISIBLE
        lottie.playAnimation()

        // Animasyon bitince geri dön
        lottie.addAnimatorListener(object : com.airbnb.lottie.LottieListener<com.airbnb.lottie.LottieComposition?>,
            android.animation.Animator.AnimatorListener {
            override fun onAnimationStart(animation: android.animation.Animator) {}
            override fun onAnimationEnd(animation: android.animation.Animator) {
                if (isAdded) findNavController().popBackStack()
            }
            override fun onAnimationCancel(animation: android.animation.Animator) {
                if (isAdded) findNavController().popBackStack()
            }
            override fun onAnimationRepeat(animation: android.animation.Animator) {}
            override fun onResult(p0: com.airbnb.lottie.LottieComposition?) {}
        })

        // Güvenlik için 1500ms sonra yine geri dön (dinleyici tetiklenmezse)
        lottie.postDelayed({
            if (isAdded) findNavController().popBackStack()
        }, 1500)
    }

    private fun setFormEnabled(enabled: Boolean) {
        binding.etTargetIban.isEnabled = enabled
        binding.etAmount.isEnabled = enabled
        binding.btnTransfer.isEnabled = enabled
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
