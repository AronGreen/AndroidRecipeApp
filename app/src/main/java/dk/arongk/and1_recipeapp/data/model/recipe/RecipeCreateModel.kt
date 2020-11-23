package dk.arongk.and1_recipeapp.data.model.recipe

import dk.arongk.and1_recipeapp.data.model.ingredientListItem.IngredientListItemCreateModel
import dk.arongk.and1_recipeapp.data.model.ingredientListItem.IngredientListItemDto
import dk.arongk.and1_recipeapp.data.model.tag.TagDto
import java.util.*

class RecipeCreateModel {
    var title: String = ""
    var workTime: Int = 0
    var totalTime: Int = 0
    var servings: Int = 0
    var description: String = ""
    var instructions: String = ""
    var notes: String = ""
    var imageUrl: String = ""
    var ingredients: MutableList<IngredientListItemCreateModel> = mutableListOf()
    var tags: MutableList<TagDto> = mutableListOf()

    fun toDto(id: UUID): RecipeDto = RecipeDto(
        id = id,
        title = this.title,
        workTime = this.workTime,
        totalTime = this.totalTime,
        servings = this.servings,
        description = this.description,
        instructions = this.instructions,
        notes = this.notes,
        imageUrl = this.imageUrl
    )
}