package com.duccipopi.baking.dao;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ducci on 09/01/2018.
 */

public class Recipe implements Parcelable {
    private int id;
    private String name;
    private Ingredient[] ingredients;
    private Step[] steps;

    Recipe(int id, String name, Ingredient[] ingredients, Step[] steps) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    protected Recipe(Parcel in) {
        id = in.readInt();
        name = in.readString();
        ingredients = in.createTypedArray(Ingredient.CREATOR);
        steps = in.createTypedArray(Step.CREATOR);
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Ingredient[] getIngredients() {
        return ingredients;
    }

    public Step[] getSteps() {
        return steps;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeTypedArray(ingredients, i);
        parcel.writeTypedArray(steps, i);
    }
}
