package com.shahaabapps.twotracks.preview

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.shahaabapps.twotracks.R

/**
 * Both launcher icons as a launcher crops them: an adaptive icon is drawn on
 * 108 units of which the middle 72 show, here under a circle and a rounded
 * square, large and at home-screen size, on a light and a dark wallpaper.
 */
@Composable
internal fun IconsPreview() {
    Column(Modifier.fillMaxSize().background(Color(0xFF808080)).padding(24.dp), verticalArrangement = Arrangement.spacedBy(24.dp)) {
        listOf(Color(0xFFEDEDED), Color(0xFF1B1B1F)).forEach { wallpaper ->
            Column(Modifier.background(wallpaper).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(R.drawable.ic_launcher_foreground, Color.White, CircleShape, 104.dp)
                    Icon(R.drawable.ic_launcher_departures_foreground, Color(0xFF131417), CircleShape, 104.dp)
                    Icon(R.drawable.ic_launcher_foreground, Color.White, RoundedCornerShape(16.dp), 56.dp)
                    Icon(R.drawable.ic_launcher_departures_foreground, Color(0xFF131417), RoundedCornerShape(16.dp), 56.dp)
                }
                Text("Parallel, then Departures", color = if (wallpaper.red > 0.5f) Color.Black else Color.White)
            }
        }
    }
}

@Composable
private fun Icon(@DrawableRes foreground: Int, background: Color, shape: Shape, size: Dp) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(size).clip(shape).background(background)) {
        Image(painterResource(foreground), contentDescription = null, modifier = Modifier.requiredSize(size * 1.5f))
    }
}
