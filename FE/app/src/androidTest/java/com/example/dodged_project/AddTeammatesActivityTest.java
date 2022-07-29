package com.example.dodged_project;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.databinding.DataBindingUtil;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.dodged_project.databinding.ActivityAddTeammatesBinding;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AddTeammatesActivityTest {

    @Rule
    public ActivityScenarioRule<AddTeammatesActivity> activityRule =
            new ActivityScenarioRule<>(AddTeammatesActivity.class);

    @Test
    public void a_typeTextInInput() {
        onView(withId(R.id.username_01_textinput)).perform(typeText("2 4"), click());
        onView(withId(R.id.username_01_textinput)).perform(scrollTo(), click());
        onView(withId(R.id.username_01_textinput)).check(matches(withText("2 4")));
    }

    @Test
    public void b_textIsRetainedFromAddTeammatesToFinalizeTeammates() {
        onView(withId(R.id.username_02_textinput)).perform(typeText("TheWanderersWay"), click());
        onView(withId(R.id.username_02_textinput)).perform(pressImeActionButton());
        onView(withId(R.id.username_02_textinput)).perform(closeSoftKeyboard());
        onView(withId(2131361945)).perform(click());
        onView(withText("TheWanderersWay")).check(matches(isDisplayed()));
    }
}