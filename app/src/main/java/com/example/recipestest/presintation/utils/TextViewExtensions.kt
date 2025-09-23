package com.example.recipestest.presintation.utils

import android.text.Html
import android.text.Spanned

fun String.toSpanned(): Spanned {
    return Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
}