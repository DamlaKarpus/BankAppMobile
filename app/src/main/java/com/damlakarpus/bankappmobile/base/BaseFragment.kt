package com.damlakarpus.bankappmobile.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    // Her fragment kendi binding'ini inflate edecek
    abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding(inflater, container)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Activity'deki BaseActivity fonksiyonlarını fragment'ten kullanmak için helper
    fun showLoading() {
        (activity as? BaseActivity)?.showLoading()
    }

    fun hideLoading() {
        (activity as? BaseActivity)?.hideLoading()
    }

    fun <T> observeResource(
        liveData: androidx.lifecycle.LiveData<Resource<T>>,
        lifecycleOwner: androidx.lifecycle.LifecycleOwner,
        onSuccess: (T) -> Unit
    ) {
        (activity as? BaseActivity)?.observeResource(liveData, lifecycleOwner, onSuccess)
    }
}
