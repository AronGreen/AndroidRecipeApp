package dk.arongk.and1_recipeapp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.findFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.room.Room
import com.google.android.material.bottomnavigation.BottomNavigationView
import dk.arongk.and1_recipeapp.data.RecipeDatabase
import dk.arongk.and1_recipeapp.data.model.recipe.RecipeCreateModel
import dk.arongk.and1_recipeapp.fragments.CreateFragment
import dk.arongk.and1_recipeapp.fragments.EditRecipeFragment
import dk.arongk.and1_recipeapp.viewModels.CreateFragmentViewModel
import java.lang.NullPointerException

class MainActivity : AppCompatActivity() {
    lateinit var db: RecipeDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment_container)
        NavigationUI.setupWithNavController(bottomNav, navController)

        db = Room.databaseBuilder(
            applicationContext,
            RecipeDatabase::class.java, "recipe_db"
        ).build()

    }
}