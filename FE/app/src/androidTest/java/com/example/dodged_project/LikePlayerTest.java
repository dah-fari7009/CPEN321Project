package com.example.dodged_project;


import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.Until;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LikePlayerTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void likePlayerTest() {
        ViewInteraction hj = onView(
                allOf(withText("Sign in"),
                        childAtPosition(
                                allOf(withId(R.id.sign_in_button),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                1)),
                                0),
                        isDisplayed()));
        hj.perform(click());

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.add_teammates_button), withText("Add Teammate"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        1),
                                0),
                        isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction textInputEditText = onView(
                allOf(withId(R.id.username_01_textinput),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("com.google.android.material.textfield.TextInputLayout")),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText.perform(replaceText("2 4"), closeSoftKeyboard());

        ViewInteraction textInputEditText2 = onView(
                allOf(withId(R.id.username_02_textinput),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("com.google.android.material.textfield.TextInputLayout")),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText2.perform(replaceText("ct819"), closeSoftKeyboard());

        ViewInteraction textInputEditText3 = onView(
                allOf(withId(R.id.username_03_textinput),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("com.google.android.material.textfield.TextInputLayout")),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText3.perform(replaceText("palukawhale"), closeSoftKeyboard());

        ViewInteraction textInputEditText4 = onView(
                allOf(withId(R.id.username_04_textinput),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("com.google.android.material.textfield.TextInputLayout")),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText4.perform(replaceText("xmovos"), closeSoftKeyboard());

        ViewInteraction textInputEditText5 = onView(
                allOf(withId(R.id.username_05_textinput),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("com.google.android.material.textfield.TextInputLayout")),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText5.perform(replaceText("TheWanderersWay"), closeSoftKeyboard());

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.confirm_button), withText("Confirm"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                0)));
        materialButton3.perform(scrollTo(), click());

        ViewInteraction materialAutoCompleteTextView = onView(
                allOf(withId(R.id.region_dropdown_item),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.region_dropdown_menu),
                                        0),
                                0)));
        materialAutoCompleteTextView.perform(scrollTo(), click());

        onView(isRoot()).perform(pressKey(KeyEvent.KEYCODE_DPAD_DOWN));
        onView(isRoot()).perform(pressKey(KeyEvent.KEYCODE_DPAD_DOWN));
        onView(isRoot()).perform(pressKey(KeyEvent.KEYCODE_DPAD_UP));
        onView(isRoot()).perform(pressKey(KeyEvent.KEYCODE_ENTER));

        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.confirm_button), withText("Confirm"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                4)));
        materialButton4.perform(scrollTo(), click());

        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        device.wait(Until.findObject(By.res("com.example.dodged_project:id/alt_playerUsername")), 5000);

        onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(0).onChildView(withId(R.id.player_like_button)).atPosition(0).perform(click()).check(matches(isDisplayed()));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
