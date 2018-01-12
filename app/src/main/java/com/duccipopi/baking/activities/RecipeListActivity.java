package com.duccipopi.baking.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.duccipopi.baking.R;
import com.duccipopi.baking.dao.Recipe;
import com.duccipopi.baking.dao.RecipesDAO;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class RecipeListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);


        View recyclerView = findViewById(R.id.recipe_list);
        assert recyclerView != null;
        RecipesDAO.listRecipes(new RecipesCallBack((RecyclerView) recyclerView));

    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Recipe> mValues;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Recipe item = (Recipe) view.getTag();
                Context context = view.getContext();
                Intent intent = new Intent(context, StepsListActivity.class);
                intent.putExtra(ActivityContract.ARG_ITEM, item);

                context.startActivity(intent);
            }
        };

        SimpleItemRecyclerViewAdapter(List<Recipe> items) {
            mValues = items;
        }

        @Override
        public SimpleItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_list_content, parent, false);
            return new SimpleItemRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final SimpleItemRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.mContentView.setText(mValues.get(position).getName());

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mContentView = view.findViewById(R.id.content);
            }
        }
    }

    // Call back to bind the data to the view
    private class RecipesCallBack implements retrofit2.Callback<List<Recipe>> {
        private final RecyclerView recyclerView;

        public RecipesCallBack(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
        }

        @Override
        public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {

            if (response.isSuccessful()) {
                recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(response.body()));
            } else
                showSnackBar(R.string.data_fetch_failed);
        }

        @Override
        public void onFailure(Call<List<Recipe>> call, Throwable t) {
            showSnackBar(R.string.data_fetch_failed);
        }

        private void showSnackBar(int resId) {
            Snackbar.make(recyclerView, getString(resId),
                    Snackbar.LENGTH_LONG).show();
        }
    }
}