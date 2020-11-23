package dk.arongk.and1_recipeapp.data.model.ingredientListItem

import java.util.*

data class IngredientListItemCreateModel(
    var quantity: Int,
    var ingredientName: String,
    var unit: String,
    var operation: String
) {
    fun toDto(id: UUID, recipeId: UUID, ingredientId: UUID): IngredientListItemDto =
        IngredientListItemDto(
            id = id,
            recipeId = recipeId,
            quantity = quantity,
            ingredientId = ingredientId,
            ingredientName = ingredientName,
            unit = unit,
            operation = operation
        )
}