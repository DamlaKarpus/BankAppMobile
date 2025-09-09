package com.damlakarpus.bankappmobile

import android.os.Bundle
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.damlakarpus.bankappmobile.base.BaseActivity
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Sistem bar padding: üst=0 (toolbar üstte zaten)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val sb = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(sb.left, 0, sb.right, sb.bottom)
            insets
        }

        // Toolbar'ı ActionBar yap
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false) // başlığı gizle
            setDisplayUseLogoEnabled(true)    // logo göster
        }

        // NavController
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Geri oku doğru yönetmesi için app bar config
        val appBarConfig = AppBarConfiguration(setOf(navController.graph.startDestinationId))
        setupActionBarWithNavController(navController, appBarConfig)

        // Toolbar sadece bu ekranlarda görünsün:
        // dashboard + transaction akışları (id'leri kendi nav_graph id'lerinle eşleştir)
        val toolbarAllowedDestinations = setOf(
            R.id.dashboardFragment,
            R.id.transactionFragment,     // para gönderme ana ekranı
            R.id.allTransactionsFragment
        )

        navController.addOnDestinationChangedListener { _, destination, _ ->
            toolbar.isVisible = destination.id in toolbarAllowedDestinations
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        return navHostFragment.navController.navigateUp() || super.onSupportNavigateUp()
    }
}
