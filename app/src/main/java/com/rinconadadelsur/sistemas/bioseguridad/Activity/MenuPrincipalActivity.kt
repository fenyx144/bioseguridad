package com.rinconadadelsur.sistemas.bioseguridad.Activity

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import com.rinconadadelsur.sistemas.bioseguridad.DataBase.dbEstructura
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.DevDataSeeder.ensureDemoData
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.ModernDrawerMenu
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.RegistroSummaryDialog
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.TransactionFormHost
import com.rinconadadelsur.sistemas.bioseguridad.Herramientas.hProcedimiento
import com.rinconadadelsur.sistemas.bioseguridad.R
import com.rinconadadelsur.sistemas.bioseguridad.databinding.ActivityMenuPrincipalBinding

class MenuPrincipalActivity : AppCompatActivity(), RegistroSummaryDialog.Listener {
    private var mAppBarConfiguration: AppBarConfiguration? = null
    private var binding: ActivityMenuPrincipalBinding? = null
    private var saveMenuItem: MenuItem? = null
    private var pendingFormHost: TransactionFormHost? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMenuPrincipalBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        ensureDemoData(this)

        setSupportActionBar(binding!!.appBarMenuPrincipal.toolbar)
        val drawer = binding!!.drawerLayout
        mAppBarConfiguration = AppBarConfiguration.Builder(R.id.nav_presentacion)
            .setOpenableLayout(drawer)
            .build()
        val navController =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_menu_principal) as NavHostFragment)
                .navController
        setupActionBarWithNavController(this, navController, mAppBarConfiguration!!)

        setupDrawerHeader()
        val menuList = findViewById<LinearLayout>(R.id.drawerMenuList)
        ModernDrawerMenu.bind(menuList, navController, drawer)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val showSave = destination.id == R.id.nav_Garita ||
                destination.id == R.id.nav_CercoElectrico ||
                destination.id == R.id.nav_Fomites
            saveMenuItem?.isVisible = showSave
        }
    }

    private fun setupDrawerHeader() {
        val header = findViewById<android.view.View>(R.id.drawerHeader)
        val hp = hProcedimiento(this, dbEstructura.miBaseDatos, null, 1)
        header.findViewById<TextView>(R.id.tvDrawerUserName)?.text =
            hp.getNombreUsuario()?.uppercase().orEmpty().ifBlank { "USUARIO" }
        val prefs = getSharedPreferences(PREFS_SYNC, Context.MODE_PRIVATE)
        header.findViewById<TextView>(R.id.tvLastDownload)?.text =
            prefs.getString(KEY_LAST_DOWNLOAD, "—")
        header.findViewById<TextView>(R.id.tvLastUpload)?.text =
            prefs.getString(KEY_LAST_UPLOAD, "—")
        val navController =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_menu_principal) as NavHostFragment)
                .navController
        header.findViewById<android.view.View>(R.id.cardDescargar)?.setOnClickListener {
            navController.navigate(R.id.nav_ImportarDB)
            binding!!.drawerLayout.closeDrawers()
        }
        header.findViewById<android.view.View>(R.id.cardSubir)?.setOnClickListener {
            navController.navigate(R.id.nav_ExportarDB)
            binding!!.drawerLayout.closeDrawers()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_principal, menu)
        saveMenuItem = menu.findItem(R.id.action_guardar_registro)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_guardar_registro) {
            val host = currentFormHost()
            if (host != null) {
                pendingFormHost = host
                RegistroSummaryDialog.newInstance(host.formTitle(), host.summaryLines())
                    .show(supportFragmentManager, "registro_resumen")
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onConfirmSave() {
        pendingFormHost?.performSave()
        pendingFormHost = null
    }

    private fun currentFormHost(): TransactionFormHost? {
        val navHost = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_content_menu_principal) as? NavHostFragment
            ?: return null
        return navHost.childFragmentManager.primaryNavigationFragment as? TransactionFormHost
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_menu_principal) as NavHostFragment)
                .navController
        return navigateUp(navController, mAppBarConfiguration!!) || super.onSupportNavigateUp()
    }

    companion object {
        const val PREFS_SYNC = "bioseg_sync"
        const val KEY_LAST_DOWNLOAD = "last_download"
        const val KEY_LAST_UPLOAD = "last_upload"
    }
}
