package com.shahaabapps.twotracks.session

import com.shahaabapps.core.data.study.StudyRepository
import com.shahaabapps.core.data.tracking.TrackingRepository
import com.shahaabapps.core.model.Availability
import com.shahaabapps.core.model.BudgetItem
import com.shahaabapps.core.model.CountryTrack
import com.shahaabapps.core.model.Deadline
import com.shahaabapps.core.model.DocumentItem
import com.shahaabapps.core.model.Habit
import com.shahaabapps.core.model.HabitLog
import com.shahaabapps.core.model.Level
import com.shahaabapps.core.model.Milestone
import com.shahaabapps.core.model.MockScore
import com.shahaabapps.core.model.StudyGoal
import com.shahaabapps.core.model.StudyLog
import com.shahaabapps.core.model.TestRecord
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Shared live collections for the current couple space. Each flow is a
 * StateFlow kept warm while any screen collects it, so switching tabs is instant
 * and Firestore listeners are not duplicated per ViewModel.
 */
@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class CoupleDataStore @Inject constructor(
    private val sessionStore: SessionStore,
    private val studyRepository: StudyRepository,
    private val trackingRepository: TrackingRepository,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private fun <T> shared(block: (String) -> Flow<List<T>>): StateFlow<List<T>> =
        sessionStore.session
            .flatMapLatest { s -> if (s == null) flowOf(emptyList()) else block(s.coupleId) }
            .stateIn(scope, SharingStarted.WhileSubscribed(10_000), emptyList())

    val goals: StateFlow<List<StudyGoal>> = shared { studyRepository.observeGoals(it) }
    val levels: StateFlow<List<Level>> = shared { studyRepository.observeLevels(it) }
    val availability: StateFlow<List<Availability>> = shared { studyRepository.observeAvailability(it) }
    val logs: StateFlow<List<StudyLog>> = shared { studyRepository.observeLogs(it) }
    val mockScores: StateFlow<List<MockScore>> = shared { studyRepository.observeMockScores(it) }
    val habits: StateFlow<List<Habit>> = shared { trackingRepository.observeHabits(it) }
    val habitLogs: StateFlow<List<HabitLog>> = shared { trackingRepository.observeHabitLogs(it) }
    val milestones: StateFlow<List<Milestone>> = shared { trackingRepository.observeMilestones(it) }
    val countries: StateFlow<List<CountryTrack>> = shared { trackingRepository.observeCountries(it) }
    val tests: StateFlow<List<TestRecord>> = shared { trackingRepository.observeTests(it) }
    val documents: StateFlow<List<DocumentItem>> = shared { trackingRepository.observeDocuments(it) }
    val budget: StateFlow<List<BudgetItem>> = shared { trackingRepository.observeBudget(it) }
    val deadlines: StateFlow<List<Deadline>> = shared { trackingRepository.observeDeadlines(it) }
}
