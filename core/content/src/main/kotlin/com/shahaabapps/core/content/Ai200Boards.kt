package com.shahaabapps.core.content

/**
 * The AI-200 episodes' boards, keyed by episode id; they live beside their
 * scripts, one function each (the JVM's 64 KB limit on a method).
 */
internal object Ai200Boards {
    val all: List<PodcastBoard> by lazy {
        Ai200EpisodesApp.boards +
            Ai200EpisodesAcr.boards +
            Ai200EpisodesAppServiceContainers.boards +
            Ai200EpisodesContainerApps.boards +
            Ai200EpisodesAks.boards +
            Ai200EpisodesContainerTroubleshoot.boards +
            Ai200EpisodesCosmosSdk.boards +
            Ai200EpisodesCosmosPerf.boards +
            Ai200EpisodesCosmosVector.boards +
            Ai200EpisodesCosmosChangeFeed.boards +
            Ai200EpisodesPostgres.boards +
            Ai200EpisodesPgvector.boards +
            Ai200EpisodesRedis.boards +
            Ai200EpisodesServiceBus.boards +
            Ai200EpisodesEventGrid.boards +
            Ai200EpisodesFunctions.boards +
            Ai200EpisodesKeyVault.boards +
            Ai200EpisodesAppConfig.boards +
            Ai200EpisodesOpenTelemetry.boards +
            Ai200EpisodesKql.boards
    }
}
