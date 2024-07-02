package com.hello.contactapproom.navigation

sealed class Routes (val route: String){
    object AllContactScreen: Routes("AllContactScreen")
    object AddEditScreen: Routes("AddEditScreen")
}