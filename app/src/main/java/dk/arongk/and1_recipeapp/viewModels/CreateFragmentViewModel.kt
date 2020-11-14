package dk.arongk.and1_recipeapp.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dk.arongk.and1_recipeapp.data.RecipeDatabase
import dk.arongk.and1_recipeapp.data.model.recipe.RecipeCreateModel
import dk.arongk.and1_recipeapp.data.repositories.RecipeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreateFragmentViewModel(application: Application) : AndroidViewModel(application) {
    var title : String = ""
    var workTime : String  = ""
    var totalTime : String  = ""
    var servings : String  = ""
    var description : String  = ""
    var instructions : String  = ""
    var notes : String  = ""
    var imageUri : String  = ""

    private val repository: RecipeRepository

    init {
        val recipeDao = RecipeDatabase.getDatabase(application, viewModelScope).recipeDao()
        val ingredientListItemDao =
            RecipeDatabase.getDatabase(application, viewModelScope).ingredientListItemDao()
        repository = RecipeRepository(recipeDao, ingredientListItemDao)
    }

    fun insert() = viewModelScope.launch(Dispatchers.IO) {
        //TODO: validate inputs
        repository.insert(
            RecipeCreateModel().also {
                it.title = title
                it.workTime = workTime.toIntOrNull() ?: 0
                it.totalTime = totalTime.toIntOrNull() ?: 0
                it.servings = servings.toIntOrNull() ?: 0
                it.description = description
                it.instructions = instructions
                it.notes = notes
                it.imageUrl = imageUri
            })
    }
}