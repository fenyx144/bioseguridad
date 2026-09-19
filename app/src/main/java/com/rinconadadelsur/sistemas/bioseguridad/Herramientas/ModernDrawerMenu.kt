package com.rinconadadelsur.sistemas.bioseguridad.Herramientas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import com.rinconadadelsur.sistemas.bioseguridad.R

object ModernDrawerMenu {
    private data class Item(
        @IdRes val destinationId: Int,
        @StringRes val titleRes: Int,
        @DrawableRes val iconRes: Int,
        @DrawableRes val badgeBg: Int,
        val tintWhite: Boolean = true
    )

    @JvmStatic
    fun bind(
        container: LinearLayout,
        navController: NavController,
        drawerLayout: DrawerLayout
    ) {
        val inflater = LayoutInflater.from(container.context)
        container.removeAllViews()

        addItem(inflater, container, navController, drawerLayout, Item(
            R.id.nav_presentacion,
            R.string.menu_Home,
            R.drawable.ic_home,
            R.drawable.bg_drawer_icon_green
        ))

        addSection(inflater, container, "Transacciones")
        addItem(inflater, container, navController, drawerLayout, Item(
            R.id.nav_Garita,
            R.string.menu_Garita,
            R.drawable.ic_account_box,
            R.drawable.bg_drawer_icon_teal
        ))
        addItem(inflater, container, navController, drawerLayout, Item(
            R.id.nav_CercoElectrico,
            R.string.menu_CercoElectrico,
            R.drawable.ic_electric_fence_24,
            R.drawable.bg_drawer_icon_amber
        ))
        addItem(inflater, container, navController, drawerLayout, Item(
            R.id.nav_Fomites,
            R.string.menu_Fomites,
            R.drawable.ic_paw_print_24,
            R.drawable.bg_drawer_icon_brown
        ))

        addSection(inflater, container, "Reportes")
        addItem(inflater, container, navController, drawerLayout, Item(
            R.id.nav_ReporteGarita,
            R.string.menu_RepGarita,
            R.drawable.ic_chart_bar_18,
            R.drawable.bg_drawer_icon_blue
        ))
        addItem(inflater, container, navController, drawerLayout, Item(
            R.id.nav_ReporteCerco,
            R.string.menu_RepCercoElectrico,
            R.drawable.ic_chart_bar_18,
            R.drawable.bg_drawer_icon_blue
        ))
        addItem(inflater, container, navController, drawerLayout, Item(
            R.id.nav_ReporteFomites,
            R.string.menu_RepFomites,
            R.drawable.ic_chart_bar_18,
            R.drawable.bg_drawer_icon_blue
        ))
    }

    private fun addSection(inflater: LayoutInflater, container: LinearLayout, title: String) {
        val section = inflater.inflate(R.layout.item_drawer_section, container, false)
        section.findViewById<TextView>(R.id.tvSectionTitle).text = title
        container.addView(section)
    }

    private fun addItem(
        inflater: LayoutInflater,
        container: LinearLayout,
        navController: NavController,
        drawerLayout: DrawerLayout,
        item: Item
    ) {
        val row = inflater.inflate(R.layout.item_drawer_menu_card, container, false)
        row.findViewById<TextView>(R.id.tvDrawerTitle).setText(item.titleRes)
        row.findViewById<FrameLayout>(R.id.iconBadge).setBackgroundResource(item.badgeBg)
        val icon = row.findViewById<ImageView>(R.id.ivDrawerIcon)
        icon.setImageResource(item.iconRes)
        if (item.tintWhite) {
            icon.setColorFilter(ContextCompat.getColor(container.context, R.color.white))
        }
        row.setOnClickListener {
            if (navController.currentDestination?.id != item.destinationId) {
                navController.navigate(item.destinationId)
            }
            drawerLayout.closeDrawers()
        }
        container.addView(row)
    }
}
