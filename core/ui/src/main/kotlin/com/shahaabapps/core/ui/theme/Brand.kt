package com.shahaabapps.core.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush

/** The one brand gradient left in Parallel: ink deepening, for illustrations and the lock mark. */
object Brand {
    val accentGradient: Brush
        @Composable get() = AppTheme.colors.heroBrush
}
