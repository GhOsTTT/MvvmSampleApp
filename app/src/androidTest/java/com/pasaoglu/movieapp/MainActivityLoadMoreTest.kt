package com.pasaoglu.movieapp


import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.pasaoglu.movieapp.data.model.MovieListItemResponseModel
import com.pasaoglu.movieapp.ui.MainActivity
import org.hamcrest.Matcher
import org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith



/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityLoadMoreTest {
    private lateinit var stringToBetyped: String
    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity>
            = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun initValidString() {
        // Specify a valid string.
        stringToBetyped = "matrix"
    }


    @Test
    fun changeText_sameActivity() {
        // Type text and then press the button.
        onView(withId(R.id.queryEditText))
            .perform(typeText(stringToBetyped), closeSoftKeyboard())
       // onView(withId(R.id.changeTextBt)).perform(click())

        // Check that the text was changed.
        //onView(withId(R.id.textToBeChanged))
        //    .check(matches(withText(stringToBetyped)))


      //  onData(withId(R.id.movieNameTextView))
      //      .check(matches(withText("matrix")));

        allOf(
            `is`(instanceOf(MovieListItemResponseModel::class.java)), hasEntry(
                contains("title"),
                `is`("The Matrix")
            )
        )

        onView(withId(R.id.recyclerView))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    1,
                    object : ViewAction {
                        override fun getConstraints(): Matcher<View>? {
                            return null
                        }

                        override fun getDescription(): String {
                            return "Click on specific button"
                        }

                        override fun perform(uiController: UiController?, view: View) {
                            val button: View = view.findViewById(R.id.container)
                            // Maybe check for null
                            button.performClick()
                        }
                    })
            )

        Espresso.pressBack()


        onView(withId(R.id.recyclerView)).perform(ScrollToBottomAction())

        Espresso.pressBack()




    }

    class ScrollToBottomAction : ViewAction {
        override fun getDescription(): String {
            return "scroll RecyclerView to bottom"
        }

        override fun getConstraints(): Matcher<View> {
            return allOf(isAssignableFrom(RecyclerView::class.java), isDisplayed())
        }

        override fun perform(uiController: UiController?, view: View?) {
            val recyclerView = view as RecyclerView
            val itemCount = recyclerView.adapter?.itemCount
            val position = itemCount?.minus(1) ?: 0
          //  lastItemCount = position
            recyclerView.scrollToPosition(position)
            uiController?.loopMainThreadUntilIdle()

            val button: View = view.findViewById(R.id.container)
            // Maybe check for null
            button.performClick()
        }
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
  //      val appContext = InstrumentationRegistry.getInstrumentation().targetContext
  //      assertEquals("com.pasaoglu.movieapp", appContext.packageName)
    }
}