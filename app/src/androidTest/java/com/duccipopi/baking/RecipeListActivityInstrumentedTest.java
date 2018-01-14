package com.duccipopi.baking;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.duccipopi.baking.activities.ActivityContract;
import com.duccipopi.baking.activities.RecipeListActivity;
import com.duccipopi.baking.activities.StepsListActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by ducci on 14/01/2018.
 */

@RunWith(AndroidJUnit4.class)
public class RecipeListActivityInstrumentedTest {

    @Rule
    public ActivityTestRule<RecipeListActivity> mActivityRule =
            new ActivityTestRule(RecipeListActivity.class);

    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Before
    public void initIntent() {
        Intents.init();
    }

    @Test
    public void test_recyclerview_perform_click() {
        onView(withId(R.id.recipe_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        intended(allOf(
                hasComponent("com.duccipopi.baking.activities.StepsListActivity"),
                hasExtraWithKey(ActivityContract.ARG_ITEM)
        ));
    }

    @Test(timeout = 10000)
    public void test_recyclerview_data_loaded() {

        onView(withId(R.id.recipe_list)).check(matches(isDisplayed()));
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }

    @After
    public void releaseIntent() {
        Intents.release();
    }

}
