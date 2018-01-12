package com.duccipopi.baking.dao;

import com.duccipopi.baking.network.RecipesService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ducci on 12/01/2018.
 */

public class RecipesDAO {

    // Base URL for Udacity recipe service
    public static final String UDACITY_BASE_URL = "http://go.udacity.com/";

    // Recipes list cache objects
    private static boolean cached = false;
    private static Call<List<Recipe>> mCachedCall;
    private static Response<List<Recipe>> mCachedResponse;

    // List recipes list (network or local) and return true if data is from local cache
    public static boolean listRecipes(retrofit2.Callback<List<Recipe>> callBack) {

        if (cached)
            callBack.onResponse(mCachedCall, mCachedResponse);
        else
            loadDataFromNetwork(callBack);

        return cached;
    }

    // Function to gather data from network
    private static void loadDataFromNetwork(retrofit2.Callback<List<Recipe>> callBack) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UDACITY_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RecipesService service = retrofit.create(RecipesService.class);

        Call<List<Recipe>> recipes = service.listRecipes();
        recipes.enqueue(new CacheCallBack(callBack));
    }


    // Callback to cache data and call calling callback
    private static class CacheCallBack implements retrofit2.Callback<List<Recipe>> {
        private retrofit2.Callback<List<Recipe>> mCallBack;

        CacheCallBack(retrofit2.Callback<List<Recipe>> callback) {
            mCallBack = callback;
        }

        @Override
        public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
            mCachedCall = call;
            mCachedResponse = response;
            cached = true;

            mCallBack.onResponse(call, response);
        }

        @Override
        public void onFailure(Call<List<Recipe>> call, Throwable t) {
            mCallBack.onFailure(call, t);
        }
    }


}
