package com.weit2nd.presentation.ui.common

import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat

@Composable
fun HtmlText(
    modifier: Modifier = Modifier,
    text: String,
) {
    val cleanedText = text.replace(Regex(HEAD_TAG_CONTENT, RegexOption.DOT_MATCHES_ALL), "")
    AndroidView(
        modifier = modifier,
        factory = { context -> TextView(context) },
        update = { it.text = HtmlCompat.fromHtml(cleanedText, HtmlCompat.FROM_HTML_MODE_COMPACT) },
    )
}

private const val HEAD_TAG_CONTENT = "<head.*?>.*?</head>"
