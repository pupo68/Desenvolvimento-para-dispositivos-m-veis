package br.edu.pucpr.listacompras.ui

import br.edu.pucpr.listacompras.R

import android.view.View
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.matcher.ViewMatchers.hasSibling
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.CoreMatchers.allOf
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented UI tests for MainActivity covering the most important UI interactions.
 * These tests are written following the TDD approach – start red, then make them green.
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityInstrumentedTest {

    @Test
    fun activityLaunch_showsRecyclerViewAndFab() {
        ActivityScenario.launch(MainActivity::class.java)
        // Verify RecyclerView is displayed
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))
        // Verify FAB is displayed
        onView(withId(R.id.fabAddItem)).check(matches(isDisplayed()))
    }

    @Test
    fun addItem_flow_successful() {
        ActivityScenario.launch(MainActivity::class.java)
        // Click FAB to open the add dialog
        onView(withId(R.id.fabAddItem)).perform(click())
        // Dialog should be displayed – title text from strings.xml
        onView(allOf(withId(R.id.dialogTitle), withText(R.string.dialog_title_add_item)))
            .check(matches(isDisplayed()))
        // Fill the form fields
        onView(withId(R.id.editNome)).perform(typeText("Teste Item"), closeSoftKeyboard())
        onView(withId(R.id.editQuantidade)).perform(typeText("2"), closeSoftKeyboard())
        // Save button should become enabled and we click it
        onView(withId(R.id.buttonSalvar)).perform(click())
        // Verify that the new item appears in the list (by checking its name text)
        onView(withText("Teste Item")).check(matches(isDisplayed()))
    }

    @Test
    fun toggleItemStatus_updatesUi() {
        ActivityScenario.launch(MainActivity::class.java)
        // Pre‑condition: we add an item first (reuse the add flow)
        onView(withId(R.id.fabAddItem)).perform(click())
        onView(withId(R.id.editNome)).perform(typeText("Toggle Test"), closeSoftKeyboard())
        onView(withId(R.id.editQuantidade)).perform(typeText("1"), closeSoftKeyboard())
        onView(withId(R.id.buttonSalvar)).perform(click())
        // The checkbox for the newly added item should be unchecked initially
        onView(allOf(isDescendantOfA(withId(R.id.recyclerView)), withText("Toggle Test")))
            .check(matches(isDisplayed()))
        // Click the checkbox to toggle status
        onView(allOf(withId(R.id.checkboxComprado), hasSibling(withText("Toggle Test")))).perform(click())
        // Verify that the text now has strikethrough (using a custom matcher could be added later)
        // For now we just ensure the checkbox is checked
        onView(allOf(withId(R.id.checkboxComprado), hasSibling(withText("Toggle Test"))))
            .check(matches(isChecked()))
    }

    @Test
    fun deleteItem_showsUndoSnackbar_andRestoresOnUndo() {
        ActivityScenario.launch(MainActivity::class.java)
        // Add an item to delete
        onView(withId(R.id.fabAddItem)).perform(click())
        onView(withId(R.id.editNome)).perform(typeText("Deletable"), closeSoftKeyboard())
        onView(withId(R.id.editQuantidade)).perform(typeText("1"), closeSoftKeyboard())
        onView(withId(R.id.buttonSalvar)).perform(click())
        // Swipe left to trigger delete (Espresso‑contrib swipe action could be used, but simple click on delete button works)
        onView(allOf(withId(R.id.buttonDelete), hasSibling(withText("Deletable")))).perform(click())
        // Confirm dialog appears and we accept deletion
        onView(withText(R.string.button_delete)).perform(click())
        // Snackbar with undo should appear
        onView(withText(R.string.snackbar_item_deleted)).check(matches(isDisplayed()))
        // Click Undo action
        onView(withText(R.string.snackbar_undo)).perform(click())
        // Verify the item is back in the list
        onView(withText("Deletable")).check(matches(isDisplayed()))
    }
}
