package dk.arongk.and1_recipeapp.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dk.arongk.and1_recipeapp.data.RecipeDatabase
import dk.arongk.and1_recipeapp.models.recipe.RecipeDto
import dk.arongk.and1_recipeapp.data.repositories.RecipeRepository
import java.util.*

class CurrentRecipeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: RecipeRepository
    var recipe: LiveData<RecipeDto>

    init {
        val db = RecipeDatabase.getDatabase(application, viewModelScope)
        repository = RecipeRepository(db.recipeDao(), db.ingredientListItemDao(), db.ingredientDao())

        recipe = MutableLiveData()
    }

    fun updateRecipe(id : UUID){
        recipe = repository.get(id);
    }

}