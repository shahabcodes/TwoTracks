package com.shahaabapps.core.content

/**
 * The boards for the styled AZ-204 episodes, keyed by episode id
 * ("az-keyvault--case-files"). They live beside their scripts, in each
 * module's Az204Styled<Module>.kt, each built by a function of its own as
 * Az204Boards' are. Line numbers are each episode's own, from 0.
 */
internal object Az204StyledBoards {
    val all: List<PodcastBoard> by lazy {
        Az204StyledCompute.boards + Az204StyledCompute2.boards + Az204StyledStorage.boards + Az204StyledStorage2.boards +
            Az204StyledSecurity.boards + Az204StyledSecurity2.boards + Az204StyledSecurity3.boards +
            Az204StyledMonitor.boards + Az204StyledMonitor2.boards + Az204StyledConnect.boards + Az204StyledConnect2.boards
    }
}
