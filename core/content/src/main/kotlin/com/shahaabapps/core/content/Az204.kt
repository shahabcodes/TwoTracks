package com.shahaabapps.core.content

/**
 * AZ-204 Developing Solutions for Microsoft Azure, written for this app in
 * plain English. The running example throughout is the Two Tracks backend:
 * an API that stores study logs and sends reminders.
 *
 * Module order and weights follow the exam skills outline.
 */
object Az204 {
    /**
     * Built once. This was a getter, so every read rebuilt the whole course —
     * every module, topic, block and question — and the screens read it
     * several times per state computation. The content never changes at run
     * time, so one copy is both correct and all that is needed.
     */
    val course: Course by lazy {
        Course(
            id = "az-204",
            title = "AZ-204 Azure Developer",
            subtitle = "Five modules, one test each, then a mock exam.",
            goalId = "az-204",
        mockExam = Az204Mock.quiz,
            modules = listOf(
                Module(
                    id = "az-compute", number = 1, title = "Compute",
                    summary = "Where your code runs: App Service, Functions and containers.",
                    examWeight = "25 to 30 percent", componentId = "compute",
                    topics = Az204Compute.topics + Az204Compute2.topics, quiz = Az204Compute2.quiz,
                ),
                Module(
                    id = "az-storage", number = 2, title = "Storage",
                    summary = "Blob storage and Cosmos DB for the data your code keeps.",
                    examWeight = "15 to 20 percent", componentId = "storage", topics = Az204Storage.topics + Az204Storage2.topics, quiz = Az204Storage2.quiz,
                ),
                Module(
                    id = "az-security", number = 3, title = "Security",
                    summary = "Signing users in, keeping secrets, and letting services talk safely.",
                    examWeight = "15 to 20 percent", componentId = "security", topics = Az204Security.topics + Az204Security2.topics, quiz = Az204Security2.quiz,
                ),
                Module(
                    id = "az-monitor", number = 4, title = "Monitor and optimise",
                    summary = "Application Insights, caching and content delivery.",
                    examWeight = "10 to 15 percent", componentId = "monitoring", topics = Az204Monitor.topics, quiz = Az204Monitor.quiz,
                ),
                Module(
                    id = "az-connect", number = 5, title = "Connect and consume",
                    summary = "API Management, events and messages between services.",
                    examWeight = "20 to 25 percent", componentId = "connect", topics = Az204Connect.topics + Az204Connect2.topics, quiz = Az204Connect2.quiz,
                ),
            ),
        )
    }
}
