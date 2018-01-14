package com.duccipopi.baking.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.duccipopi.baking.R;
import com.duccipopi.baking.dao.Ingredient;
import com.duccipopi.baking.dao.Recipe;
import com.duccipopi.baking.dao.Step;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

/**
 * A fragment representing a single Recipe detail screen.
 * This fragment is either contained in a {@link StepsListActivity}
 * in two-pane mode (on tablets) or a {@link StepActivity}
 * on handsets.
 */
public class IngredientsFragment extends Fragment {


    private Recipe mRecipe;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public IngredientsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ActivityContract.ARG_ITEM)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mRecipe = getArguments().getParcelable(ActivityContract.ARG_ITEM);

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.ingredients_detail, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.ingredients_list);
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(Arrays.asList(mRecipe.getIngredients())));

        return rootView;
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Ingredient> mValues;

        SimpleItemRecyclerViewAdapter(List<Ingredient> items) {
            mValues = items;
        }

        @Override
        public SimpleItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ingredient_list_content, parent, false);
            return new SimpleItemRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final SimpleItemRecyclerViewAdapter.ViewHolder holder, int position) {
            Ingredient ingredient = mValues.get(position);
            holder.mQuantity.setText(Float.toString(ingredient.getQuantity()));
            holder.mMeasure.setText(ingredient.getMeasure());
            holder.mName.setText(ingredient.getName());

        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mQuantity;
            final TextView mMeasure;
            final TextView mName;

            ViewHolder(View view) {
                super(view);
                mQuantity = view.findViewById(R.id.quantity);
                mMeasure = view.findViewById(R.id.measure);
                mName = view.findViewById(R.id.name);
            }
        }
    }

}
