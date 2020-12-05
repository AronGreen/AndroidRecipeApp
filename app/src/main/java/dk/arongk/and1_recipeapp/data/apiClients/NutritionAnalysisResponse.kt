package dk.arongk.and1_recipeapp.data.apiClients

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * check out https://rapidapi.com/edamam/api/edamam-nutrition-analysis/endpoints
 * for possible data to get, for now I will focus on calories, but future data may include
 *  - 'healthLabels' and 'dietLabels' for adding tags to a given recipe
 *  - proportion of recommended daily intake
 *  - breakdown of calorie contents into fat, sugar, etc.
 */
class NutritionAnalysisResponse(
    @Expose
    @SerializedName("calories")
    val calories: Int
)