package dk.arongk.and1_recipeapp.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import dk.arongk.and1_recipeapp.data.RecipeDatabase
import dk.arongk.and1_recipeapp.models.recipe.RecipeDto
import dk.arongk.and1_recipeapp.data.repositories.RecipeRepository

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: RecipeRepository
    var recipes: LiveData<List<RecipeDto>>

    init {
        val db = RecipeDatabase.getDatabase(application, viewModelScope)
        repository = RecipeRepository(db.recipeDao(), db.ingredientListItemDao(), db.ingredientDao())

        recipes = repository.allRecipes
    }

    fun updateRecipes(){
        recipes = repository.allRecipes
    }
}