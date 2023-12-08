package com.unex.asee.ga02.beergo.view.home


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.unex.asee.ga02.beergo.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class SettingsTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(LoginActivity::class.java)





    @Test
    fun settingsTest2() {

        val materialButton = onView(
            allOf(
                withId(R.id.bt_register), withText("Unete!"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    6
                ),
                isDisplayed()
            )
        )
        materialButton.perform(click())

        try {
            Thread.sleep(2000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        val appCompatEditText = onView(
            allOf(
                withId(R.id.et_username),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatEditText.perform(replaceText("1234"), closeSoftKeyboard())

        val appCompatEditText2 = onView(
            allOf(
                withId(R.id.et_password),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        appCompatEditText2.perform(replaceText("1234"), closeSoftKeyboard())

        val appCompatEditText3 = onView(
            allOf(
                withId(R.id.et_repassword),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    6
                ),
                isDisplayed()
            )
        )
        appCompatEditText3.perform(replaceText("1234"), closeSoftKeyboard())

        val appCompatEditText4 = onView(
            allOf(
                withId(R.id.et_repassword), withText("1234"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    6
                ),
                isDisplayed()
            )
        )
        appCompatEditText4.perform(pressImeActionButton())

        val materialButton2 = onView(
            allOf(
                withId(R.id.bt_register), withText("¡Unete!"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    7
                ),
                isDisplayed()
            )
        )
        materialButton2.perform(click())

        try {
            Thread.sleep(2000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        val materialButton3 = onView(
            allOf(
                withId(R.id.bt_login), withText("Inicar Sesion"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    5
                ),
                isDisplayed()
            )
        )
        materialButton3.perform(click())

        try {
            Thread.sleep(2000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        val imageView = onView(
            allOf(
                withContentDescription("More options"),
                withParent(withParent(withId(R.id.toolbar))),
                isDisplayed()
            )
        )
        imageView.check(matches(isDisplayed()))

        val overflowMenuButton = onView(
            allOf(
                withContentDescription("More options"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.toolbar),
                        1
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        overflowMenuButton.perform(click())

        try {
            Thread.sleep(2000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        val linearLayout = onView(
            allOf(
                withId(androidx.appcompat.R.id.content),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.ListView::class.java))),
                isDisplayed()
            )
        )
        linearLayout.check(matches(isDisplayed()))

        val materialTextView = onView(
            allOf(
                withId(R.id.title), withText("settings"),
                childAtPosition(
                    childAtPosition(
                        withId(androidx.appcompat.R.id.content),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        materialTextView.perform(click())

        try {
            Thread.sleep(2000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        val switch_ = onView(
            allOf(
                withId(android.R.id.switch_widget),
                withParent(
                    allOf(
                        withId(android.R.id.widget_frame),
                        withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        switch_.check(matches(isDisplayed()))

        val textView = onView(
            allOf(
                withText("SettingsFragment"),
                withParent(
                    allOf(
                        withId(R.id.toolbar),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        textView.check(matches(withText("SettingsFragment")))

        val textView2 = onView(
            allOf(
                withId(android.R.id.title), withText("Recuerdame"),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        textView2.check(matches(withText("Recuerdame")))

    }


    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
