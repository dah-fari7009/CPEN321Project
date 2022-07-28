package com.example.dodged_project;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ResultsActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mainActivityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    public static ViewAction waitFor(final long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "Wait for " + millis + " milliseconds.";
            }

            @Override
            public void perform(UiController uiController, final View view) {
                uiController.loopMainThreadForAtLeast(millis);
            }
        };
    }

    @Test
    public void getToResultsActivity() {
        onView(withId(R.id.continue_as_guest_button)).perform(click());

        onView(withId(R.id.add_teammates_button)).perform(click());

        onView(withId(R.id.username_01_textinput)).perform(typeText("2 4"), click());
        onView(withId(R.id.username_01_textinput)).perform(pressImeActionButton());
        onView(withId(R.id.username_02_textinput)).perform(typeText("TheWanderersWay"), click());
        onView(withId(R.id.username_02_textinput)).perform(pressImeActionButton());
        onView(withId(R.id.username_03_textinput)).perform(typeText("palukawhale"), click());
        onView(withId(R.id.username_03_textinput)).perform(pressImeActionButton());
        onView(withId(R.id.username_04_textinput)).perform(typeText("Thick Rooster"), click());
        onView(withId(R.id.username_04_textinput)).perform(pressImeActionButton());
        onView(withId(R.id.username_05_textinput)).perform(typeText("ct819"), click());
        onView(withId(R.id.username_05_textinput)).perform(pressImeActionButton());
        onView(withId(R.id.username_05_textinput)).perform(closeSoftKeyboard());
        onView(isRoot()).perform(waitFor(2000));
        onView(withId(2131361945)).perform(click());

        onView(withId(R.id.region_dropdown_menu)).perform(click());
        onView(withId(R.id.region_dropdown_menu)).perform(swipeUp());
        onView(withText("NA1")).perform(click());
    }
//
//    @Rule
//    public ActivityScenarioRule<ResultsActivity> activityRule =
//            new ActivityScenarioRule<>(ResultsActivity.class);

//    @Test
//    public void test() {
//        onView(withId(R.id.player_like_button)).perform(click());
//    }
}