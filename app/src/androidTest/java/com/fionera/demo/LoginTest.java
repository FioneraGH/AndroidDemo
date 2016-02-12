package com.fionera.demo;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by fionera on 16-2-12.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);


    @Test
    public void loginWithWrongPassword() {
        onView(withId(R.id.actv_login_email)).perform(typeText("hello~"), closeSoftKeyboard());
        onView(withId(R.id.email_sign_in_button)).perform(click());

        onView(withId(R.id.title_bar_title)).check(matches(withText("登录失败")));

    }


    @Test
    public void loginWithRightPassword() {
        onView(withId(R.id.actv_login_email)).perform(typeText("hello"), closeSoftKeyboard());
        onView(withId(R.id.email_sign_in_button)).perform(click());

        onView(withId(R.id.title_bar_title)).check(matches(withText("登录成功")));

    }
}