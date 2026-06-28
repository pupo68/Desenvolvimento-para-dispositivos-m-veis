package br.edu.pucpr.listacompras.util

import android.view.View

/**
 * Extension functions for Android Views
 * Improves code readability and follows Kotlin idiomatic practices
 */

/** Makes the view visible */
fun View.visible() {
    visibility = View.VISIBLE
}

/** Makes the view gone (removed from layout) */
fun View.gone() {
    visibility = View.GONE
}

/** Makes the view invisible (occupies space but not visible) */
fun View.invisible() {
    visibility = View.INVISIBLE
}

/** Toggles visibility based on a condition */
fun View.visibleIf(condition: Boolean) {
    visibility = if (condition) View.VISIBLE else View.GONE
}
