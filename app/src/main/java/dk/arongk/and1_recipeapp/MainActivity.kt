package dk.arongk.and1_recipeapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration

class MainActivity : AppCompatActivity()  {
//    val navController by lazy {findNavController(R.id.nav_host_fragment)}
//    val appBarConfiguration by lazy {
//        AppBarConfiguration(navController.graph, findViewById(R.id.bottom_navigation))
//    }
    val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
    val navController = navHostFragment.navController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}