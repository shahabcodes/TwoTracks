package com.shahaabapps.core.content

/**
 * The AZ-305 episodes: every topic in all five tellings, as AI-200's are, one
 * file a topic (Az305Episodes<Topic>.kt), gathered here.
 */
internal object Az305Podcasts {
    val all: List<Podcast> by lazy {
        Az305EpisodesMonitoring.podcasts +
            Az305EpisodesIdentity.podcasts +
            Az305EpisodesAuthorization.podcasts +
            Az305EpisodesGovernance.podcasts +
            Az305EpisodesRelational.podcasts +
            Az305EpisodesNonRelational.podcasts +
            Az305EpisodesDataIntegration.podcasts +
            Az305EpisodesBackupDr.podcasts +
            Az305EpisodesHighAvailability.podcasts +
            Az305EpisodesCompute.podcasts +
            Az305EpisodesAppArchitecture.podcasts +
            Az305EpisodesMigrations.podcasts +
            Az305EpisodesNetworkConnectivity.podcasts +
            Az305EpisodesNetworkOptimize.podcasts
    }
}
