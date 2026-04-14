package com.tutushubham.studypartner.core.ui.layout

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Centers content like Stitch HTML `max-w-md` / `max-w-lg mx-auto` for phones vs tablets.
 *
 * **Fundamentals:** constrain readable line length on large screens; avoid ultra-wide forms.
 * **Alternative:** [androidx.compose.material3.adaptive] navigation pane — heavier dependency.
 */
@Composable
fun StitchMaxWidthColumn(
    modifier: Modifier = Modifier,
    /** Stitch profile/settings use ~448dp (`max-w-md`). Basic info uses ~512dp (`max-w-lg`). */
    maxContentWidth: Dp = 448.dp,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit,
) {
    BoxWithConstraints(modifier.fillMaxSize()) {
        val innerMax = minOf(maxContentWidth, this.maxWidth)
        Column(
            Modifier
                .fillMaxWidth()
                .widthIn(max = innerMax)
                .align(Alignment.TopCenter),
            horizontalAlignment = horizontalAlignment,
            content = content,
        )
    }
}
