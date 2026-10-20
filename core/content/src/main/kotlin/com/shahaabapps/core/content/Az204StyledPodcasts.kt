package com.shahaabapps.core.content

/**
 * AZ-204 topics told another way, beside their classic episodes and never
 * instead of them; the listener picks one in the player. The facts, the
 * beginner's plain words, the exam's take-aways and the thing to try today
 * are the classic episode's. What changes is the telling:
 *
 * - [PodcastStyle.CASE_FILES]: a mystery. Something in a small app has gone
 *   wrong; Leo is the junior detective who suspects the wrong thing, Maya
 *   walks the clues to the service that solves it, the way an exam question
 *   reads a scenario and asks for the answer.
 * - [PodcastStyle.GOSSIP]: the office gossip. What a colleague did (always a
 *   made-up one: Priya, Tom, the intern; never a real person) and what everyone
 *   learnt from it, the topic's mistakes told as the week's scandal.
 *
 * Both also say what a thing really is under its everyday picture, how it
 * works, where it stops working, and which episode carries on from it
 * (tools/podcast/WRITING.md). Each module's episodes and boards are in a file
 * of their own, Az204Styled<Module>.kt; StyledPodcastTest holds their shape.
 */
internal object Az204StyledPodcasts {
    val all: List<Podcast> by lazy {
        Az204StyledCompute.podcasts + Az204StyledCompute2.podcasts + Az204StyledStorage.podcasts + Az204StyledStorage2.podcasts +
            Az204StyledSecurity.podcasts + Az204StyledSecurity2.podcasts + Az204StyledSecurity3.podcasts +
            Az204StyledMonitor.podcasts + Az204StyledMonitor2.podcasts + Az204StyledConnect.podcasts + Az204StyledConnect2.podcasts
    }
}
