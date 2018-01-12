package com.duccipopi.baking.dao;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ducci on 09/01/2018.
 */

public class Ingredient implements Parcelable {
    private float quantity;
    private String measure;

    @SerializedName("ingredient")
    private String name;

    public Ingredient(int quantity, String measure, String name) {
        this.quantity = quantity;
        this.measure = measure;
        this.name = name;
    }

    protected Ingredient(Parcel in) {
        quantity = in.readFloat();
        measure = in.readString();
        name = in.readString();
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    public float getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeFloat(quantity);
        parcel.writeString(measure);
        parcel.writeString(name);
    }
}
