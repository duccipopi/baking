package com.duccipopi.baking;

import com.duccipopi.baking.dao.Ingredient;
import com.duccipopi.baking.dao.Recipe;
import com.duccipopi.baking.dao.Step;

/**
 * Created by ducci on 14/01/2018.
 */

public class Util {
    private static final int NRO_INGREDIENTS = 4;
    private static final int NRO_STEPS = 5;

    public static Recipe getRecipe() {
        if (sRecipe == null) {
            sRecipe = new Recipe(1, "Testing recipe", getIngredients(NRO_INGREDIENTS), getSteps(NRO_STEPS), "");
        }

        return sRecipe;
    }

    private static Step[] getSteps(int nroSteps) {
        Step[] steps = new Step[nroSteps];
        for (int i = 0; i < nroSteps; i++) {
            steps[i] = new Step(i, "short" + i, "desc" + i, "", "");
        }

        return steps;
    }

    private static Ingredient[] getIngredients(int nroIngredients) {
        Ingredient[] ingredients = new Ingredient[nroIngredients];
        for (int i = 0; i < nroIngredients; i++) {
            ingredients[i] = new Ingredient(i, "m"+i, "n"+i);
        }

        return ingredients;
    }

    private static Recipe sRecipe;
}
