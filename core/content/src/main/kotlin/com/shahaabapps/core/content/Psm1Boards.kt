package com.shahaabapps.core.content

/** The PSM I episodes' boards, keyed by episode id; they live beside their scripts. */
internal object Psm1Boards {
    val all: List<PodcastBoard> by lazy {
        Psm1EpisodesEmpiricism.boards +
        Psm1EpisodesValues.boards +
        Psm1EpisodesScrumTeam.boards +
        Psm1EpisodesDevelopers.boards +
        Psm1EpisodesProductOwner.boards +
        Psm1EpisodesScrumMaster.boards +
        Psm1EpisodesSprint.boards +
        Psm1EpisodesSprintPlanning.boards +
        Psm1EpisodesDailyScrum.boards +
        Psm1EpisodesReviewRetro.boards +
        Psm1EpisodesProductBacklog.boards +
        Psm1EpisodesSprintBacklog.boards +
        Psm1EpisodesIncrement.boards +
        Psm1EpisodesTraps.boards +
        Psm1EpisodesAssessment.boards
    }
}
