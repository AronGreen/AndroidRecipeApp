package dk.arongk.and1_recipeapp.viewModels

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import dk.arongk.and1_recipeapp.R
import dk.arongk.and1_recipeapp.data.RecipeDatabase
import dk.arongk.and1_recipeapp.data.model.ingredientListItem.IngredientListItemCreateModel
import dk.arongk.and1_recipeapp.data.model.recipe.RecipeCreateModel
import dk.arongk.and1_recipeapp.data.repositories.RecipeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreateViewModel(application: Application) : AndroidViewModel(application) {
    var title: String = ""
    var workTime: String = ""
    var totalTime: String = ""
    var servings: String = ""
    var description: String = ""
    var instructions: String = ""
    var notes: String = ""
    var imageUri: String = ""
    var ingredients: MutableList<IngredientListItemCreateModel>;

    private val repository: RecipeRepository

    init {
        val db = RecipeDatabase.getDatabase(application, viewModelScope)
        repository =
            RecipeRepository(db.recipeDao(), db.ingredientListItemDao(), db.ingredientDao())
        ingredients = mutableListOf(IngredientListItemCreateModel(1, "Banana", "", "Chopped"))
    }

    fun insert(model: RecipeCreateModel, navController: NavController, activity: Activity, fragment: Fragment)  = viewModelScope.launch(Dispatchers.IO) {
        // TODO: validate inputs
        val id = repository.insert(model)

        activity.runOnUiThread {
            val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
            with (sharedPref.edit()) {
                putString(fragment.getString(R.string.current_recipe_id_string), id.toString())
                apply()
            }
            navController.navigate(R.id.action_createFragment_to_currentRecipeFragment)
        }
    }


}