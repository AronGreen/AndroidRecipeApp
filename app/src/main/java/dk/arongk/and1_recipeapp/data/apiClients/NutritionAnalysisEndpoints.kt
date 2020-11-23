package dk.arongk.and1_recipeapp.data.apiClients

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NutritionAnalysisEndpoints {

    @GET("/api/nutrition-data")
    fun getNutritionAnalysis(@Query("ingr") ingredient: String): Call<NutritionAnalysisResponse>
}