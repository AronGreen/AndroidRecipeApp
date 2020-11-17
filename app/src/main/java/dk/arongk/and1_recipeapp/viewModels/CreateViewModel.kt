package dk.arongk.and1_recipeapp.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dk.arongk.and1_recipeapp.data.RecipeDatabase
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

    private val repository: RecipeRepository

    init {
        val recipeDao = RecipeDatabase.getDatabase(application, viewModelScope).recipeDao()
        val ingredientListItemDao =
            RecipeDatabase.getDatabase(application, viewModelScope).ingredientListItemDao()
        repository = RecipeRepository(recipeDao, ingredientListItemDao)
    }

    fun insert(
        title: String,
        workTime: Int,
        totalTime: Int,
        servings: Int,
        description: String,
        instructions: String,
        notes: String,
        imageUrl: String
    ) = viewModelScope.launch(Dispatchers.IO) {
        //TODO: validate inputs
        val model = RecipeCreateModel()
        model.title = title
        model.workTime = workTime
        model.totalTime = totalTime
        model.servings = servings
        model.description = description
        model.instructions = instructions
        model.notes = notes
        model.imageUrl = imageUrl

        repository.insert(model)
    }
}