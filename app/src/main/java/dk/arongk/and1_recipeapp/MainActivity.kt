package dk.arongk.and1_recipeapp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.findFragment
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.room.Room
import com.google.android.material.bottomnavigation.BottomNavigationView
import dk.arongk.and1_recipeapp.data.RecipeDatabase
import dk.arongk.and1_recipeapp.data.model.recipe.RecipeCreateModel
import dk.arongk.and1_recipeapp.fragments.CreateFragment
import dk.arongk.and1_recipeapp.viewModels.CreateFragmentViewModel

class MainActivity : AppCompatActivity() {
    //    val navController by lazy {findNavController(R.id.nav_host_fragment)}
//    val appBarConfiguration by lazy {
//        AppBarConfiguration(navController.graph, findViewById(R.id.bottom_navigation))
//    }
    lateinit var db: RecipeDatabase

    lateinit var createFragment : CreateFragment

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

    fun onClickBtn(view: View) {
        Toast.makeText(this, "Clicked on Button", Toast.LENGTH_LONG).show();

        createFragment  = view.findFragment()
        createFragment.createFun()

//        when(view){
//           is CreateFragment ->
//        }
//        var createModel = RecipeCreateModel()
//        createModel.title = view.findViewById(R.id.title).text.toString()
//        createModel.workTime = workTime.text.toString().toIntOrNull() ?: 0
//        createModel.totalTime = totalTime.text.toString().toIntOrNull() ?:0
//        createModel.servings = servings.text.toString().toIntOrNull() ?:0
//        createModel.description = description.text.toString()
//        createModel.instructions = instructions.text.toString()
//        createModel.notes = notes.text.toString()
//        createModel.imageUrl = imageUrl.text.toString()
//
//        db.recipeDao().insert()

    }
}