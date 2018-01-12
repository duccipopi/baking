package com.duccipopi.baking.network;

import com.duccipopi.baking.dao.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by ducci on 09/01/2018.
 */

public interface RecipesService {
    @GET("android-baking-app-json")
    Call<List<Recipe>> listRecipes();
}
