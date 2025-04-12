package com.example.musicappui

import androidx.annotation.DrawableRes

sealed class Screen(val title: String, val route: String) {
    sealed class DrawerScreens(val dTitle: String, val dRoute: String, @DrawableRes val icon: Int) :
        Screen(dTitle, dRoute) {
        object Account : DrawerScreens("Account", "account", R.drawable.ic_account)
        object Subscribed : DrawerScreens("Subscribed", "subscribed", R.drawable.ic_subscriptions)
        object AddAccount :
            DrawerScreens("Add Account", "add_account", R.drawable.baseline_person_add_alt_1_24)
    }
}


val screensInDrawer = listOf(
    Screen.DrawerScreens.Account,
    Screen.DrawerScreens.Subscribed,
    Screen.DrawerScreens.AddAccount
)