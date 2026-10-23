package com.shahaabapps.core.content

/**
 * The PSM I episodes: every topic in all five tellings, as AI-200's and
 * AZ-305's are, one file a topic (Psm1Episodes<Topic>.kt), gathered here.
 */
internal object Psm1Podcasts {
    val all: List<Podcast> by lazy {
        Psm1EpisodesEmpiricism.podcasts +
        Psm1EpisodesValues.podcasts +
        Psm1EpisodesScrumTeam.podcasts +
        Psm1EpisodesDevelopers.podcasts +
        Psm1EpisodesProductOwner.podcasts +
        Psm1EpisodesScrumMaster.podcasts +
        Psm1EpisodesSprint.podcasts +
        Psm1EpisodesSprintPlanning.podcasts +
        Psm1EpisodesDailyScrum.podcasts +
        Psm1EpisodesReviewRetro.podcasts +
        Psm1EpisodesProductBacklog.podcasts +
        Psm1EpisodesSprintBacklog.podcasts +
        Psm1EpisodesIncrement.podcasts +
        Psm1EpisodesTraps.podcasts +
        Psm1EpisodesAssessment.podcasts
    }
}
