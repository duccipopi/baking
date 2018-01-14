package com.duccipopi.baking.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.duccipopi.baking.R;
import com.duccipopi.baking.dao.Recipe;
import com.duccipopi.baking.dao.Step;

import java.util.Arrays;
import java.util.List;

/**
 * An activity representing a list of Recipes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link StepActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class StepsListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps_list);

        mRecipe = getRecipeFromIntent();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(mRecipe.getName());

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setTitle(mRecipe.getName());

        if (findViewById(R.id.step_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }


        View recyclerView = findViewById(R.id.step_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        View ingredientView = findViewById(R.id.ingredient_card);
        ingredientView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    invokeActivityOrFragment(IngredientsFragment.class, IngredientsActivity.class, mTwoPane, mRecipe);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Recipe getRecipeFromIntent() {
        Intent callingIntent = getIntent();

        if (callingIntent.hasExtra(ActivityContract.ARG_ITEM)) {
            return (Recipe) callingIntent.getParcelableExtra(ActivityContract.ARG_ITEM);
        } else finish();
        return null;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        List<Step> steps = Arrays.asList(mRecipe.getSteps());
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(steps, mTwoPane));
    }


    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Step> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Step item = (Step) view.getTag();
                try {
                    invokeActivityOrFragment(StepFragment.class, StepActivity.class, mTwoPane, item);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }
        };

        SimpleItemRecyclerViewAdapter(List<Step> items,
                                      boolean twoPane) {
            mValues = items;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.step_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mIdView.setText(Integer.toString(mValues.get(position).getId()));
            holder.mContentView.setText(mValues.get(position).getShortDescription());

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mIdView = view.findViewById(R.id.id_text);
                mContentView = view.findViewById(R.id.content);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Choose between Fragment and activity to display info
    private void invokeActivityOrFragment(Class<? extends Fragment> fragmentClass, Class<? extends Activity> activityClass,
                                          boolean isTwoPane, Parcelable item)
            throws IllegalAccessException, InstantiationException {

        if (isTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(ActivityContract.ARG_ITEM, item);
            Fragment fragment = fragmentClass.newInstance();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, activityClass);
            intent.putExtra(ActivityContract.ARG_ITEM, item);

            startActivity(intent);
        }
    }

}
