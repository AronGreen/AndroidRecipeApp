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

    var title: String = ""
    var workTime: String = ""
    var totalTime: String = ""
    var servings: String = ""
    var description: String = ""
    var instructions: String = ""
    var notes: String = ""
    var imageUrl: String = ""

    private val repository: RecipeRepository
    val createModel: RecipeCreateModel = RecipeCreateModel()

    init {
        val recipeDao = RecipeDatabase.getDatabase(application, viewModelScope).recipeDao()
        val ingredientListItemDao =
            RecipeDatabase.getDatabase(application, viewModelScope).ingredientListItemDao()
        repository = RecipeRepository(recipeDao, ingredientListItemDao)
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(recipe: RecipeCreateModel) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(recipe)
    }

    fun insert(
        title: String,
        workTime: String,
        totalTime: String,
        servings: String,
        description: String,
        instructions: String,
        notes: String,
        imageUrl: String
    ) = viewModelScope.launch(Dispatchers.IO) {
        val model = RecipeCreateModel()
        model.title = title
        model.workTime = workTime.toIntOrNull() ?: 0
        model.totalTime = totalTime.toIntOrNull() ?: 0
        model.servings = servings.toIntOrNull() ?: 0
        model.description = description
        model.instructions = instructions
        model.notes = notes
        model.imageUrl = imageUrl

        repository.insert(model)
    }

}