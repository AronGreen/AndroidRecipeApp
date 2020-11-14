package dk.arongk.and1_recipeapp.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import dk.arongk.and1_recipeapp.data.RecipeDatabase
import dk.arongk.and1_recipeapp.data.model.recipe.RecipeCreateModel
import dk.arongk.and1_recipeapp.data.model.recipe.RecipeDto
import dk.arongk.and1_recipeapp.data.repositories.RecipeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel(application: Application) : AndroidViewModel(application) {
    private lateinit var recipes : LiveData<List<RecipeDto>>

    private val repository: RecipeRepository

    init {
        val recipeDao = RecipeDatabase.getDatabase(application, viewModelScope).recipeDao()
        val ingredientListItemDao =
            RecipeDatabase.getDatabase(application, viewModelScope).ingredientListItemDao()
        repository = RecipeRepository(recipeDao, ingredientListItemDao)

        viewModelScope.launch(Dispatchers.IO) {
            recipes = repository.getAll()
        }
    }
}