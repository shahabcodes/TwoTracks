package com.shahaabapps.core.content

/**
 * The AI-200 episodes: every topic as Classic (Maya and Leo), Case Files and
 * Gossip (Maya and Leo again), Late Night (Nova and Ivy) and Couples (Nova and
 * Rafe), written as tools/podcast/WRITING.md says. Each module's episodes live
 * in a file of their own, Ai200Podcasts<Module>.kt, gathered here.
 */
internal object Ai200Podcasts {
    val all: List<Podcast> by lazy {
        Ai200EpisodesAcr.podcasts +
            Ai200EpisodesAppServiceContainers.podcasts +
            Ai200EpisodesContainerApps.podcasts +
            Ai200EpisodesAks.podcasts +
            Ai200EpisodesContainerTroubleshoot.podcasts +
            Ai200EpisodesCosmosSdk.podcasts +
            Ai200EpisodesCosmosPerf.podcasts +
            Ai200EpisodesCosmosVector.podcasts +
            Ai200EpisodesCosmosChangeFeed.podcasts +
            Ai200EpisodesPostgres.podcasts +
            Ai200EpisodesPgvector.podcasts +
            Ai200EpisodesRedis.podcasts +
            Ai200EpisodesServiceBus.podcasts +
            Ai200EpisodesEventGrid.podcasts +
            Ai200EpisodesFunctions.podcasts +
            Ai200EpisodesKeyVault.podcasts +
            Ai200EpisodesAppConfig.podcasts +
            Ai200EpisodesOpenTelemetry.podcasts +
            Ai200EpisodesKql.podcasts
    }
}
