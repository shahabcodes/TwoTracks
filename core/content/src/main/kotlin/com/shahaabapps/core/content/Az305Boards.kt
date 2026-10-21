package com.shahaabapps.core.content

/** The AZ-305 episodes' boards, keyed by episode id; they live beside their scripts. */
internal object Az305Boards {
    val all: List<PodcastBoard> by lazy {
        Az305EpisodesMonitoring.boards +
            Az305EpisodesIdentity.boards +
            Az305EpisodesAuthorization.boards +
            Az305EpisodesGovernance.boards +
            Az305EpisodesRelational.boards +
            Az305EpisodesNonRelational.boards +
            Az305EpisodesDataIntegration.boards +
            Az305EpisodesBackupDr.boards +
            Az305EpisodesHighAvailability.boards +
            Az305EpisodesCompute.boards +
            Az305EpisodesAppArchitecture.boards +
            Az305EpisodesMigrations.boards +
            Az305EpisodesNetworkConnectivity.boards +
            Az305EpisodesNetworkOptimize.boards
    }
}
