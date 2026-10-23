package com.shahaabapps.twotracks.feature.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.JetBrainsFamily
import com.shahaabapps.twotracks.ui.board.BoardButton
import com.shahaabapps.twotracks.ui.board.BoardField
import com.shahaabapps.twotracks.ui.board.BoardFooter
import com.shahaabapps.twotracks.ui.board.BoardLabel
import com.shahaabapps.twotracks.ui.board.BoardLink
import com.shahaabapps.twotracks.ui.board.BoardMono
import com.shahaabapps.twotracks.ui.board.BoardNotice
import com.shahaabapps.twotracks.ui.board.BoardSegments
import com.shahaabapps.twotracks.ui.board.BoardSteps
import com.shahaabapps.twotracks.ui.board.BoardText
import com.shahaabapps.twotracks.ui.board.BoardTitle
import com.shahaabapps.twotracks.ui.board.Perforation
import com.shahaabapps.twotracks.ui.board.BoardBack
import com.shahaabapps.twotracks.ui.board.FlipTiles
import java.util.Locale

/** What the sign-in screens can ask for, so the board's version needs no view model. */
class AuthActions(
    val onEmail: (String) -> Unit,
    val onPassword: (String) -> Unit,
    val onConfirm: (String) -> Unit,
    val onMode: (AuthMode) -> Unit,
    val onSubmit: () -> Unit,
)

/**
 * Sign-in, sign-up and the reset desk on the Departures board, as its
 * prototype draws them: signing in is checking in, with the form as a
 * boarding pass torn along its perforation.
 */
@Composable
fun ColumnScope.BoardAuth(state: AuthUiState, actions: AuthActions) {
    when (state.mode) {
        AuthMode.SIGN_IN -> BoardSignIn(state, actions)
        AuthMode.SIGN_UP -> BoardSignUp(state, actions)
        AuthMode.RESET -> BoardReset(state, actions)
    }
}

@Composable
private fun ColumnScope.BoardSignIn(state: AuthUiState, actions: AuthActions) {
    var shown by remember { mutableStateOf(false) }
    Row(modifier = Modifier.padding(top = 46.dp).fillMaxWidth()) {
        BoardMono("DOH ⇄ KHI", modifier = Modifier.weight(1f))
        BoardMono("TERMINAL · THE MOVE")
    }
    BoardTitle("Two\nTracks", size = 64.sp, modifier = Modifier.padding(top = 10.dp))
    BoardText("Check in to see today's board for both of you.", modifier = Modifier.padding(top = 12.dp))
    Column(modifier = Modifier.padding(top = 28.dp).fillMaxWidth().clip(RoundedCornerShape(18.dp)).background(AppTheme.colors.card)) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            BoardField(
                state.email, actions.onEmail, label = "Passenger · email", mono = true, deep = true,
                keyboardType = KeyboardType.Email, isError = state.email.isNotEmpty() && !state.emailValid,
            )
            BoardField(
                state.password, actions.onPassword, label = "Password", deep = true, keyboardType = KeyboardType.Password,
                visualTransformation = if (shown) VisualTransformation.None else PasswordVisualTransformation(),
                labelTrailing = { BoardLink("Forgot?", onClick = { actions.onMode(AuthMode.RESET) }) },
                trailing = { ShowPassword(shown) { shown = !shown } },
                textStyle = TextStyle(fontSize = 17.sp, color = AppTheme.colors.ink),
            )
        }
        Perforation()
        Box(Modifier.padding(16.dp)) { BoardButton("Check in", onClick = actions.onSubmit, enabled = state.canSubmit) }
    }
    BoardMessages(state)
    Spacer(Modifier.weight(1f).heightIn(min = 32.dp))
    BoardFooter("First time flying?", "Create your space", onClick = { actions.onMode(AuthMode.SIGN_UP) }, modifier = Modifier.padding(bottom = 32.dp))
}

@Composable
private fun ColumnScope.BoardSignUp(state: AuthUiState, actions: AuthActions) {
    var shown by remember { mutableStateOf(false) }
    val transform = if (shown) VisualTransformation.None else PasswordVisualTransformation()
    BoardSteps(1, 3, onBack = { actions.onMode(AuthMode.SIGN_IN) }, backLabel = "Back to sign in")
    BoardTitle("Issue your pass", modifier = Modifier.padding(top = 18.dp))
    BoardText("One space, two passengers. You can invite your partner once it is open.", modifier = Modifier.padding(top = 8.dp))
    Column(modifier = Modifier.padding(top = 22.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        BoardField(state.email, actions.onEmail, label = "Email", keyboardType = KeyboardType.Email, isError = state.email.isNotEmpty() && !state.emailValid)
        BoardField(
            state.password, actions.onPassword, label = "Password", placeholder = "6 characters or more", keyboardType = KeyboardType.Password,
            visualTransformation = transform, trailing = { ShowPassword(shown) { shown = !shown } },
        )
        val mismatch = state.confirmPassword.isNotEmpty() && !state.passwordsMatch
        BoardField(state.confirmPassword, actions.onConfirm, label = "Same again", keyboardType = KeyboardType.Password, visualTransformation = transform, isError = mismatch)
        BoardStrength(state.password, state.confirmPassword)
    }
    BoardMessages(state)
    BoardButton("Continue", onClick = actions.onSubmit, enabled = state.canSubmit, modifier = Modifier.padding(top = 22.dp))
    Spacer(Modifier.weight(1f).heightIn(min = 32.dp))
    BoardFooter("Already checked in?", "Sign in", onClick = { actions.onMode(AuthMode.SIGN_IN) }, modifier = Modifier.padding(bottom = 28.dp))
}

@Composable
private fun ColumnScope.BoardReset(state: AuthUiState, actions: AuthActions) {
    BoardBack({ actions.onMode(AuthMode.SIGN_IN) }, label = "Back to sign in")
    BoardLabel("Lost pass desk", color = AppTheme.colors.accent, size = 14.sp, weight = FontWeight.Bold, modifier = Modifier.padding(top = 18.dp))
    BoardTitle("Reset your password", modifier = Modifier.padding(top = 6.dp))
    BoardText("We will send a link to the email you signed up with. Your plan and progress stay exactly where they are.", modifier = Modifier.padding(top = 10.dp))
    BoardField(state.email, actions.onEmail, label = "Email", mono = true, keyboardType = KeyboardType.Email, modifier = Modifier.padding(top = 26.dp), isError = state.email.isNotEmpty() && !state.emailValid)
    BoardButton(if (state.infoMessage != null) "Send again" else "Send reset link", onClick = actions.onSubmit, enabled = state.canSubmit, arrow = false, modifier = Modifier.padding(top = 20.dp))
    AnimatedVisibility(visible = state.infoMessage != null, enter = expandVertically() + fadeIn(), exit = shrinkVertically() + fadeOut()) {
        BoardNotice(
            "Link sent", (state.infoMessage.orEmpty() + " If nothing arrives in a few minutes, look in spam, then send it again.").trim(),
            AppTheme.colors.success, AppTheme.colors.allowedBackground, Modifier.padding(top = 16.dp).semantics { liveRegion = LiveRegionMode.Polite },
        )
    }
    AnimatedVisibility(visible = state.errorMessage != null, enter = expandVertically() + fadeIn(), exit = shrinkVertically() + fadeOut()) {
        BoardNotice("Not sent", state.errorMessage.orEmpty(), AppTheme.colors.destructive, AppTheme.colors.blockedBackground, Modifier.padding(top = 16.dp))
    }
    Spacer(Modifier.weight(1f).heightIn(min = 32.dp))
    BoardFooter("", "Back to check-in", onClick = { actions.onMode(AuthMode.SIGN_IN) }, modifier = Modifier.padding(bottom = 32.dp))
}

@Composable
private fun BoardMessages(state: AuthUiState) {
    AnimatedVisibility(visible = state.errorMessage != null, enter = expandVertically() + fadeIn(), exit = shrinkVertically() + fadeOut()) {
        BoardNotice("Not boarded", state.errorMessage.orEmpty(), AppTheme.colors.destructive, AppTheme.colors.blockedBackground, Modifier.padding(top = 16.dp).semantics { liveRegion = LiveRegionMode.Polite })
    }
    AnimatedVisibility(visible = state.infoMessage != null, enter = expandVertically() + fadeIn(), exit = shrinkVertically() + fadeOut()) {
        BoardNotice("Check your inbox", state.infoMessage.orEmpty(), AppTheme.colors.success, AppTheme.colors.allowedBackground, Modifier.padding(top = 16.dp))
    }
}

/** Four segments for the password and one line saying how it stands: "STRONG · PASSWORDS MATCH". */
@Composable
private fun BoardStrength(password: String, confirm: String) {
    val score = passwordScore(password)
    val color = when (score) {
        0 -> AppTheme.colors.separator
        1 -> AppTheme.colors.destructive
        2 -> AppTheme.colors.warn
        else -> AppTheme.colors.success
    }
    val words = boardStrengthWords(score, confirm.isNotEmpty(), confirm == password)
    val mismatch = confirm.isNotEmpty() && confirm != password
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.weight(1f)) {
            repeat(4) { i -> Box(Modifier.weight(1f).height(4.dp).clip(RoundedCornerShape(2.dp)).background(if (i < score) color else AppTheme.colors.separator)) }
        }
        if (words.isNotEmpty()) BoardLabel(words, color = if (mismatch) AppTheme.colors.destructive else color, size = 13.sp, weight = FontWeight.Bold, spacing = 0.1.em)
    }
}

/** "STRONG · PASSWORDS MATCH", "FAIR", "WEAK · THEY DIFFER". */
internal fun boardStrengthWords(score: Int, confirmed: Boolean, match: Boolean): String {
    val strength = when (score) { 0 -> ""; 1 -> "Weak"; 2 -> "Fair"; 3 -> "Good"; else -> "Strong" }
    val agreement = when { !confirmed -> ""; match -> "Passwords match"; else -> "They differ" }
    return listOf(strength, agreement).filter { it.isNotEmpty() }.joinToString(" · ").uppercase(Locale.ROOT)
}

@Composable
private fun ShowPassword(shown: Boolean, onToggle: () -> Unit) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(40.dp).clip(CircleShape).clickable(role = Role.Button, onClick = onToggle)) {
        Icon(
            if (shown) Icons.Filled.VisibilityOff else Icons.Filled.Visibility, contentDescription = if (shown) "Hide password" else "Show password",
            tint = AppTheme.colors.secondaryLabel, modifier = Modifier.size(20.dp),
        )
    }
}

// ------------------------------------------------------------------ setting up

/** Choosing your line, step 2 of 3: the developer's or the teacher's, and the clock you live on. */
@Composable
fun ColumnScope.BoardRoleStep(
    name: String,
    role: PersonRole,
    timezone: String,
    onName: (String) -> Unit,
    onRole: (PersonRole) -> Unit,
    onContinue: () -> Unit,
    onSignOut: () -> Unit,
) {
    BoardSteps(2, 3, onBack = null)
    BoardTitle("Choose your line", modifier = Modifier.padding(top = 18.dp))
    BoardText("Your courses, your levels and the countries you lead all follow from this.", modifier = Modifier.padding(top = 8.dp))
    // Not in this step of the prototype, which asks for the name a step earlier:
    // the app asks for it here, as the name printed on the pass.
    BoardField(name, onName, label = "Name on the pass", modifier = Modifier.padding(top = 20.dp))
    Column(modifier = Modifier.padding(top = 18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        LineCard("DEV", "The developer", "AZ-204 · German", "Germany, Ireland, Netherlands", AppTheme.colors.tech, role == PersonRole.TECH) { onRole(PersonRole.TECH) }
        LineCard("EDU", "The teacher", "IELTS · German", "Canada, New Zealand", AppTheme.colors.teacher, role == PersonRole.TEACHER) { onRole(PersonRole.TEACHER) }
    }
    Column(modifier = Modifier.padding(top = 16.dp)) {
        Box(Modifier.fillMaxWidth().height(1.dp).background(AppTheme.colors.separator))
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp).padding(vertical = 8.dp)) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                BoardText("Local clock", color = AppTheme.colors.ink, weight = FontWeight.Medium)
                BoardText("Days, streaks and reminders run on it", size = 12.sp)
            }
            BoardMono(timezone.uppercase(Locale.ROOT), color = AppTheme.colors.accent, size = 13.sp, spacing = 0.sp)
        }
        Box(Modifier.fillMaxWidth().height(1.dp).background(AppTheme.colors.separator))
    }
    Spacer(Modifier.weight(1f).heightIn(min = 24.dp))
    BoardButton("Continue", onClick = onContinue, enabled = name.isNotBlank())
    BoardFooter("Not you?", "Sign out", onClick = onSignOut, modifier = Modifier.padding(top = 4.dp, bottom = 24.dp))
}

@Composable
private fun LineCard(code: String, title: String, studies: String, leads: String, color: Color, selected: Boolean, onSelect: () -> Unit) {
    val shape = RoundedCornerShape(16.dp)
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth().clip(shape).background(AppTheme.colors.card)
            .border(if (selected) 2.dp else 1.dp, if (selected) color else AppTheme.colors.separator, shape)
            .semantics { this.selected = selected }.clickable(role = Role.RadioButton, onClick = onSelect).padding(16.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.height(26.dp).clip(RoundedCornerShape(6.dp)).background(color).padding(horizontal = 10.dp)) {
                BoardLabel(code, color = AppTheme.colors.groupedBackground, size = 15.sp, weight = FontWeight.Bold, spacing = 0.1.em)
            }
            Spacer(Modifier.weight(1f))
            if (selected) BoardLabel("Selected", color = color, size = 13.sp, weight = FontWeight.Bold, spacing = 0.12.em)
        }
        Text28(title)
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Fact("Studies", studies)
            Fact("Leads", leads)
        }
    }
}

@Composable
private fun Text28(text: String) {
    androidx.compose.material3.Text(
        text, color = AppTheme.colors.ink,
        style = TextStyle(fontFamily = com.shahaabapps.core.ui.theme.CondensedFamily, fontWeight = FontWeight.SemiBold, fontSize = 28.sp, lineHeight = 28.sp),
    )
}

@Composable
private fun Fact(label: String, value: String) {
    Row {
        BoardLabel(label, size = 13.sp, spacing = 0.12.em, modifier = Modifier.width(70.dp))
        BoardText(value, color = AppTheme.colors.ink, size = 13.sp)
    }
}

/** The space, step 3 of 3: open a new terminal, or board your partner's with their code. */
@Composable
fun ColumnScope.BoardSpaceStep(
    joining: Boolean,
    inviteCode: String,
    error: String?,
    submitting: Boolean,
    canSubmit: Boolean,
    onJoining: (Boolean) -> Unit,
    onCode: (String) -> Unit,
    onSubmit: () -> Unit,
    onBack: () -> Unit,
) {
    BoardSteps(3, 3, onBack = onBack)
    BoardTitle("One terminal, two lines", modifier = Modifier.padding(top = 18.dp))
    BoardSegments(listOf(false to "Start new", true to "Join your partner's"), joining, onJoining, modifier = Modifier.padding(top = 18.dp))
    if (!joining) {
        BoardText(
            "Both plans come already loaded: ten levels, six courses, six countries and the paperwork. Once the terminal is open, this code brings your partner's line in.",
            modifier = Modifier.padding(top = 18.dp),
        )
        Column(
            modifier = Modifier.padding(top = 14.dp).fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(AppTheme.colors.card).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            BoardLabel("Invite code", size = 13.sp)
            // The code is issued when the space is created, so the tiles wait
            // blank until then and Settings shows it for good.
            FlipTiles("······", tileHeight = 58.dp, fontSize = 30.sp, gap = 5.dp, fill = true, color = AppTheme.colors.secondaryLabel)
            BoardText("Issued when the terminal opens, and kept in Settings.", size = 13.sp)
        }
    } else {
        BoardText("Type the six-character code from your partner's phone. You board their space as the other line.", modifier = Modifier.padding(top = 18.dp))
        BoardField(
            inviteCode, onCode, label = "Code", keyboardType = KeyboardType.Ascii, height = 64.dp, modifier = Modifier.padding(top = 10.dp),
            textStyle = TextStyle(fontFamily = JetBrainsFamily, fontWeight = FontWeight.Bold, fontSize = 30.sp, letterSpacing = 0.3.em, color = AppTheme.colors.accent),
        )
    }
    error?.let { BoardNotice("Not boarded", it, AppTheme.colors.destructive, AppTheme.colors.blockedBackground, Modifier.padding(top = 16.dp)) }
    Spacer(Modifier.weight(1f).heightIn(min = 24.dp))
    BoardButton(
        when {
            submitting -> "Setting up"
            joining -> "Board the space"
            else -> "Open the terminal"
        },
        onClick = onSubmit, enabled = canSubmit, modifier = Modifier.padding(bottom = 36.dp),
    )
}

// ------------------------------------------------------------------ the gate

/** The lock, as a closed gate: LOCKED on flip tiles, and a ring to touch. */
@Composable
fun BoardLock(date: String, time: String, error: String?, onUnlock: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
    ) {
        Row(modifier = Modifier.padding(top = 46.dp).fillMaxWidth()) {
            BoardMono(date, modifier = Modifier.weight(1f))
            BoardMono(time)
        }
        FlipTiles("LOCKED", tileWidth = 44.dp, tileHeight = 60.dp, fontSize = 32.sp, tile = AppTheme.colors.card, modifier = Modifier.padding(top = 90.dp))
        BoardTitle("Gate closed", size = 32.sp, modifier = Modifier.padding(top = 24.dp), textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        BoardText(
            "Your plan, your partner's progress and the documents stay behind your fingerprint.",
            textAlign = androidx.compose.ui.text.style.TextAlign.Center, modifier = Modifier.padding(top = 8.dp).width(290.dp),
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(top = 52.dp).size(96.dp).clip(CircleShape).border(2.dp, AppTheme.colors.accent, CircleShape)
                .clickable(role = Role.Button, onClickLabel = "Unlock with fingerprint", onClick = onUnlock),
        ) { Icon(androidx.compose.material.icons.Icons.Filled.Fingerprint, contentDescription = "Unlock with fingerprint", tint = AppTheme.colors.accent, modifier = Modifier.size(44.dp)) }
        BoardLabel("Touch to board", size = 14.sp, modifier = Modifier.padding(top = 14.dp))
        error?.let { BoardText(it, color = AppTheme.colors.destructive, size = 13.sp, textAlign = androidx.compose.ui.text.style.TextAlign.Center, modifier = Modifier.padding(top = 16.dp).semantics { liveRegion = LiveRegionMode.Polite }) }
    }
}
