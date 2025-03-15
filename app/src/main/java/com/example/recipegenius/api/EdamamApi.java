package com.example.recipegenius.api;

import com.example.recipegenius.model.RecipeResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface EdamamApi {
    @GET("?type=public")
    Call<RecipeResponse> searchRecipes(
            @Query("q") String ingredients,
            @Query("app_id") String appId,
            @Query("app_key") String appKey,
            @Query("cuisineType") String cuisineType,
            @Query("health") String health
    );
}