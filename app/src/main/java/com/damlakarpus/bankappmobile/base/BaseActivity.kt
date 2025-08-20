package com.damlakarpus.bankappmobile.base

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

open class BaseActivity : AppCompatActivity() {

    private var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    // Activity içindeki ProgressBar'ı set et
    fun setProgressBar(pb: ProgressBar) {
        progressBar = pb
    }

    // Resource<T> için genel observer
    fun <T> observeResource(liveData: LiveData<Resource<T>>, onSuccess: (T) -> Unit) {
        liveData.observe(this, Observer { resource ->
            when (resource) {
                is Resource.Loading -> progressBar?.visibility = View.VISIBLE
                is Resource.Success -> {
                    progressBar?.visibility = View.GONE
                    resource.data?.let { onSuccess(it) }
                }
                is Resource.Error -> {
                    progressBar?.visibility = View.GONE
                    Toast.makeText(this, resource.message ?: "Bilinmeyen hata", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    // İsteğe bağlı eski show/hide Loading metotları
    fun showLoading() {
        progressBar?.visibility = View.VISIBLE
    }

    fun hideLoading() {
        progressBar?.visibility = View.GONE
    }
}
