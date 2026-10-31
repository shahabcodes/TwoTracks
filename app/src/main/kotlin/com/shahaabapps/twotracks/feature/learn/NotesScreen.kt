package com.shahaabapps.twotracks.feature.learn

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shahaabapps.core.content.Courses
import com.shahaabapps.core.content.Notes
import com.shahaabapps.core.content.TopicNotes
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.ui.components.DetailScaffold

/**
 * The notes sheet for one topic, which is also the sheet for its episode.
 *
 * There is no view model and nothing to load: a sheet is content, the same as
 * a topic's blocks, and the only thing the route carries is which topic. It
 * draws through [BlockView], so a sheet can hold nothing the lesson screen
 * cannot already show — diagrams, tables, comparisons, commands and the rest
 * all arrive drawn and themed, in both looks, for free.
 */
@Composable
fun NotesScreen(courseId: String, topicId: String, onBack: () -> Unit) {
    val notes = Notes.forTopic(topicId)
    val course = Courses.all.firstOrNull { it.id == courseId }
    val title = course?.allTopics?.firstOrNull { it.id == topicId }?.title
    NotesContent(notes = notes, topicTitle = title, courseTitle = course?.title, onBack = onBack)
}

/** The sheet drawn from content alone, so a preview and a test can show it. */
@Composable
fun NotesContent(
    notes: TopicNotes?,
    topicTitle: String?,
    courseTitle: String?,
    onBack: () -> Unit,
) {
    DetailScaffold(
        title = topicTitle ?: "Notes",
        onBack = onBack,
        eyebrow = listOfNotNull(courseTitle, "IMPORTANT NOTES").joinToString(" · ").uppercase(),
        modifier = Modifier.testTag(NOTES_SCREEN),
    ) { inner ->
        LazyColumn(
            contentPadding = PaddingValues(
                start = Spacing.listHorizontal, end = Spacing.listHorizontal,
                top = inner.calculateTopPadding() + Spacing.sm, bottom = Spacing.xl * 2,
            ),
            verticalArrangement = Arrangement.spacedBy(Spacing.md),
        ) {
            if (notes == null) {
                item {
                    Text(
                        "This topic has no notes sheet yet.",
                        style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel,
                    )
                }
                return@LazyColumn
            }
            item {
                // The headline is the whole sheet in a sentence, and the only
                // thing on it written for this screen rather than reused.
                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        notes.headline,
                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 17.sp, lineHeight = 24.sp),
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        sheetSummary(notes),
                        style = MaterialTheme.typography.labelMedium, color = AppTheme.colors.secondaryLabel,
                    )
                }
            }
            items(notes.blocks.size) { i -> BlockView(notes.blocks[i]) }
        }
    }
}

/** "6 definitions · 1 diagram · 5 commands", so the sheet says what it holds. */
internal fun sheetSummary(notes: TopicNotes): String = listOfNotNull(
    notes.definitions.size.takeIf { it > 0 }?.let { "$it definition${if (it == 1) "" else "s"}" },
    notes.pictures.takeIf { it > 0 }?.let { "$it diagram${if (it == 1) "" else "s"}" },
    notes.tables.takeIf { it > 0 }?.let { "$it table${if (it == 1) "" else "s"}" },
    notes.commands.size.takeIf { it > 0 }?.let { "$it command${if (it == 1) "" else "s"}" },
).joinToString(" · ").uppercase()

/**
 * The way into the sheet from the lesson, drawn like the listen card above it:
 * what it is, what is on it, and a chevron. Shown only where a sheet exists,
 * so a topic without one carries no dead row.
 */
@Composable
fun NotesCard(notes: TopicNotes, onOpen: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(Spacing.rule, AppTheme.colors.separator, RoundedCornerShape(16.dp))
            .clickable(role = Role.Button, onClickLabel = "Open the notes sheet", onClick = onOpen)
            .padding(horizontal = Spacing.md, vertical = Spacing.sm)
            .testTag(NOTES_CARD),
    ) {
        Icon(Icons.Filled.PushPin, contentDescription = null, tint = AppTheme.colors.accentText, modifier = Modifier.size(20.dp))
        Column(modifier = Modifier.weight(1f).padding(start = Spacing.sm)) {
            Text("Important notes", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
            Text(sheetSummary(notes), style = MaterialTheme.typography.labelMedium, color = AppTheme.colors.secondaryLabel)
        }
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = AppTheme.colors.secondaryLabel)
    }
}

const val NOTES_SCREEN = "notes-screen"
const val NOTES_CARD = "notes-card"
