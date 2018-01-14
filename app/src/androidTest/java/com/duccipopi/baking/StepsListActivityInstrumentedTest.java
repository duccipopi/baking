package com.duccipopi.baking;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.duccipopi.baking.activities.ActivityContract;
import com.duccipopi.baking.activities.StepsListActivity;
import com.duccipopi.baking.dao.Step;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;

/**
 * Created by ducci on 14/01/2018.
 */
@RunWith(AndroidJUnit4.class)
public class StepsListActivityInstrumentedTest {

    @Rule
    public ActivityTestRule<StepsListActivity> mActivityRule =
            new ActivityTestRule<StepsListActivity>(StepsListActivity.class) {

                @Override
                protected Intent getActivityIntent() {
                    Intent intent = new Intent(
                            InstrumentationRegistry.getInstrumentation().getTargetContext(),
                            StepsListActivity.class);
                    intent.putExtra(ActivityContract.ARG_ITEM, Util.getRecipe());

                    return intent;
                }
            };

    @Before
    public void initIntent() {
        Intents.init();
    }

    @Test
    public void test_ingredient_card_displayed() {

        onView(withId(R.id.ingredient_card)).check(matches(isDisplayed()));
    }

    @Test
    public void test_step_list_displayed() {

        onView(withId(R.id.step_list)).check(matches(isDisplayed()));
    }

    @Test
    public void test_all_steps_displayed() {

        int i = 0;
        for (Step step : Util.getRecipe().getSteps()) {
            onView(withId(R.id.step_list)).perform(RecyclerViewActions.scrollToPosition(i++));
            onView(withText(step.getShortDescription())).check(matches(isDisplayed()));
        }
    }

    @Test
    public void test_step_perform_click() {

        int i = (new Random()).nextInt(Util.getRecipe().getSteps().length);

        onView(withId(R.id.step_list)).perform(RecyclerViewActions.actionOnItemAtPosition(i, click()));
        intended(allOf(
                anyOf(
                        hasComponent("com.duccipopi.baking.activities.StepActivity"),
                        hasComponent("com.duccipopi.baking.activities.StepFragment")
                ),
                hasExtraWithKey(ActivityContract.ARG_ITEM)
        ));
    }

    @After
    public void releaseIntent() {
        Intents.release();
    }
}
