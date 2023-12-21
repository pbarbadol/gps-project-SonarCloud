package com.unex.asee.ga02.beergo.view.home


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.unex.asee.ga02.beergo.R
import com.unex.asee.ga02.beergo.api.ModoPrueba
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.`is`
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class StadisticsTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(LoginActivity::class.java)

    @Before
    fun setUp() {
        ModoPrueba.modoPrueba = true
    }

    @Test
    fun stadisticsTest() {
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
        Thread.sleep(2000)


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
        appCompatEditText.perform(replaceText("espresso"), closeSoftKeyboard())

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
        appCompatEditText2.perform(replaceText("latte"), closeSoftKeyboard())

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
        appCompatEditText3.perform(replaceText("latte"), closeSoftKeyboard())

        val appCompatEditText4 = onView(
            allOf(
                withId(R.id.et_repassword), withText("latte"),
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
        Thread.sleep(2000)

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
        Thread.sleep(2000)

        val bottomNavigationItemView = onView(
            allOf(
                withId(R.id.profileFragment), withContentDescription("Perfil"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottomNavigationView),
                        0
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView.perform(click())
        Thread.sleep(2000)

        val textView = onView(
            allOf(
                withId(R.id.cervezasAnadidas), withText("Cervezas Añadidas: 0"),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java))),
                isDisplayed()
            )
        )
        textView.check(matches(withText("Cervezas Añadidas: 0")))

        val textView2 = onView(
            allOf(
                withId(R.id.cervezasFavoritas), withText("Cervezas Favoritas: 0"),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java))),
                isDisplayed()
            )
        )
        textView2.check(matches(withText("Cervezas Favoritas: 0")))

        val textView3 = onView(
            allOf(
                withId(R.id.comentariosAnadidos), withText("Comentarios Añadidos: 0"),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java))),
                isDisplayed()
            )
        )
        textView3.check(matches(withText("Comentarios Añadidos: 0")))

        val textView4 = onView(
            allOf(
                withId(R.id.logrosConseguidos), withText("Logros Conseguidos: 0"),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java))),
                isDisplayed()
            )
        )
        textView4.check(matches(withText("Logros Conseguidos: 0")))

        val bottomNavigationItemView2 = onView(
            allOf(
                withId(R.id.listFragment), withContentDescription("Lista"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottomNavigationView),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView2.perform(click())
        Thread.sleep(2000)

        val appCompatImageButton = onView(
            allOf(
                withId(R.id.btnAddBeer),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        appCompatImageButton.perform(click())
        Thread.sleep(2000)

        val appCompatEditText5 = onView(
            allOf(
                withId(R.id.editTextBeerName),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText5.perform(replaceText("cerv1"), closeSoftKeyboard())

        val appCompatEditText6 = onView(
            allOf(
                withId(R.id.editTextBeerDescription),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatEditText6.perform(replaceText("desc"), closeSoftKeyboard())

        val appCompatEditText7 = onView(
            allOf(
                withId(R.id.editTextYear),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatEditText7.perform(replaceText("2023"), closeSoftKeyboard())

        val appCompatEditText8 = onView(
            allOf(
                withId(R.id.editTextAlcoholPercentage),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        appCompatEditText8.perform(replaceText("5"), closeSoftKeyboard())

        val appCompatEditText9 = onView(
            allOf(
                withId(R.id.editTextAlcoholPercentage), withText("5"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        appCompatEditText9.perform(pressImeActionButton())

        val materialButton4 = onView(
            allOf(
                withId(R.id.buttonInsertBeer), withText("Insertar cerveza"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    6
                ),
                isDisplayed()
            )
        )
        materialButton4.perform(click())
        Thread.sleep(2000)

        val appCompatEditText10 = onView(
            allOf(
                withId(R.id.editTextBeerName), withText("cerv1"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText10.perform(replaceText("cerv2"))

        val appCompatEditText11 = onView(
            allOf(
                withId(R.id.editTextBeerName), withText("cerv2"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText11.perform(closeSoftKeyboard())

        val appCompatEditText12 = onView(
            allOf(
                withId(R.id.editTextBeerName), withText("cerv2"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText12.perform(pressImeActionButton())

        val appCompatEditText13 = onView(
            allOf(
                withId(R.id.editTextBeerDescription), withText("desc"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatEditText13.perform(pressImeActionButton())

        val appCompatEditText14 = onView(
            allOf(
                withId(R.id.editTextYear), withText("2023"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatEditText14.perform(pressImeActionButton())

        val appCompatEditText15 = onView(
            allOf(
                withId(R.id.editTextAlcoholPercentage), withText("5"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        appCompatEditText15.perform(pressImeActionButton())

        val materialButton5 = onView(
            allOf(
                withId(R.id.buttonInsertBeer), withText("Insertar cerveza"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    6
                ),
                isDisplayed()
            )
        )
        materialButton5.perform(click())
        Thread.sleep(2000)

        val appCompatEditText16 = onView(
            allOf(
                withId(R.id.editTextBeerName), withText("cerv2"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText16.perform(replaceText("cerv3"))

        val appCompatEditText17 = onView(
            allOf(
                withId(R.id.editTextBeerName), withText("cerv3"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText17.perform(closeSoftKeyboard())

        val materialButton6 = onView(
            allOf(
                withId(R.id.buttonInsertBeer), withText("Insertar cerveza"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    6
                ),
                isDisplayed()
            )
        )
        materialButton6.perform(click())
        Thread.sleep(2000)

        val appCompatEditText18 = onView(
            allOf(
                withId(R.id.editTextBeerName), withText("cerv3"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText18.perform(click())
        Thread.sleep(2000)

        val appCompatEditText19 = onView(
            allOf(
                withId(R.id.editTextBeerName), withText("cerv3"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText19.perform(replaceText("cerv4"))

        val appCompatEditText20 = onView(
            allOf(
                withId(R.id.editTextBeerName), withText("cerv4"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText20.perform(closeSoftKeyboard())

        val materialButton7 = onView(
            allOf(
                withId(R.id.buttonInsertBeer), withText("Insertar cerveza"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    6
                ),
                isDisplayed()
            )
        )
        materialButton7.perform(click())
        Thread.sleep(2000)

        val appCompatImageButton2 = onView(
            allOf(
                withContentDescription("Navigate up"),
                childAtPosition(
                    allOf(
                        withId(R.id.toolbar),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            2
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatImageButton2.perform(click())
        Thread.sleep(2000)

        val recyclerView = onView(
            allOf(
                withId(R.id.rv_beer_list),
                childAtPosition(
                    withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                    2
                )
            )
        )
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(0, click()))
        Thread.sleep(2000)

        val switch_ = onView(
            allOf(
                withId(R.id.favSwitch), withText("Favorita "),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                        1
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        switch_.perform(click())
        Thread.sleep(2000)

        val appCompatImageView = onView(
            allOf(
                withId(R.id.imageView7),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                        1
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatImageView.perform(click())
        Thread.sleep(2000)

        val materialButton8 = onView(
            allOf(
                withId(R.id.addCommentButton), withText("Añadir Comentario"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        materialButton8.perform(click())
        Thread.sleep(2000)

        val appCompatEditText21 = onView(
            allOf(
                withId(R.id.editTextText),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText21.perform(replaceText("me encanta"), closeSoftKeyboard())

        val materialButton9 = onView(
            allOf(
                withId(R.id.btAccept), withText("Aceptar"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        materialButton9.perform(click())
        Thread.sleep(2000)

        val appCompatImageButton3 = onView(
            allOf(
                withContentDescription("Navigate up"),
                childAtPosition(
                    allOf(
                        withId(R.id.toolbar),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            2
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatImageButton3.perform(click())
        Thread.sleep(2000)

        val appCompatImageButton4 = onView(
            allOf(
                withContentDescription("Navigate up"),
                childAtPosition(
                    allOf(
                        withId(R.id.toolbar),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            2
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatImageButton4.perform(click())
        Thread.sleep(2000)

        val recyclerView2 = onView(
            allOf(
                withId(R.id.rv_beer_list),
                childAtPosition(
                    withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                    2
                )
            )
        )
        recyclerView2.perform(actionOnItemAtPosition<ViewHolder>(3, click()))
        Thread.sleep(2000)

        val appCompatImageView2 = onView(
            allOf(
                withId(R.id.imageView7),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                        1
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatImageView2.perform(click())
        Thread.sleep(2000)

        val materialButton10 = onView(
            allOf(
                withId(R.id.addCommentButton), withText("Añadir Comentario"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        materialButton10.perform(click())
        Thread.sleep(2000)

        val appCompatEditText22 = onView(
            allOf(
                withId(R.id.editTextText),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText22.perform(replaceText("esta no"), closeSoftKeyboard())

        val materialButton11 = onView(
            allOf(
                withId(R.id.btAccept), withText("Aceptar"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_host_fragment),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        materialButton11.perform(click())
        Thread.sleep(2000)

        val appCompatImageButton5 = onView(
            allOf(
                withContentDescription("Navigate up"),
                childAtPosition(
                    allOf(
                        withId(R.id.toolbar),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            2
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatImageButton5.perform(click())
        Thread.sleep(2000)

        val appCompatImageButton6 = onView(
            allOf(
                withContentDescription("Navigate up"),
                childAtPosition(
                    allOf(
                        withId(R.id.toolbar),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            2
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatImageButton6.perform(click())
        Thread.sleep(2000)

        val bottomNavigationItemView3 = onView(
            allOf(
                withId(R.id.profileFragment), withContentDescription("Perfil"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottomNavigationView),
                        0
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView3.perform(click())
        Thread.sleep(2000)

        val textView5 = onView(
            allOf(
                withId(R.id.cervezasAnadidas), withText("Cervezas Añadidas: 4"),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java))),
                isDisplayed()
            )
        )
        textView5.check(matches(withText("Cervezas Añadidas: 4")))

        val textView6 = onView(
            allOf(
                withId(R.id.cervezasFavoritas), withText("Cervezas Favoritas: 1"),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java))),
                isDisplayed()
            )
        )
        textView6.check(matches(withText("Cervezas Favoritas: 1")))

        val textView7 = onView(
            allOf(
                withId(R.id.comentariosAnadidos), withText("Comentarios Añadidos: 2"),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java))),
                isDisplayed()
            )
        )
        textView7.check(matches(withText("Comentarios Añadidos: 2")))

        val textView8 = onView(
            allOf(
                withId(R.id.logrosConseguidos), withText("Logros Conseguidos: 3"),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java))),
                isDisplayed()
            )
        )
        textView8.check(matches(withText("Logros Conseguidos: 3")))
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
