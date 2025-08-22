package com.damlakarpus.bankappmobile.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.damlakarpus.bankappmobile.R

class DashboardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // activity_main'deki hello world layoutunu buraya taşı
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }
}
