package com.shahaabapps.core.content

/**
 * AI-200 Developing AI Cloud Solutions on Azure, the developer exam that
 * replaced AZ-204 when it retired on 31 July 2026, written for this app in
 * plain English from the official study guide (skills measured as of
 * 7 October 2026) and Microsoft Learn. Code is in Python, as the exam's
 * audience profile asks.
 *
 * Module order and weights follow the exam skills outline.
 */
object Ai200 {
    /** Built once, as AZ-204 is: the content never changes at run time. */
    val course: Course by lazy {
        Course(
            id = "ai-200",
            title = "AI-200 Azure AI Cloud Developer",
            subtitle = "Four modules, one test each, then five mock exams.",
            goalId = "ai-200",
            mockExam = Ai200Mock.quiz,
            extraMocks = listOf(Ai200Mock3.quiz, Ai200Mock4.quiz, Ai200Mock5.quiz, Ai200Mock6.quiz),
            modules = listOf(
                Module(
                    id = "ai-containers", number = 1, title = "Containers",
                    summary = "Images in Container Registry, and running them on App Service, Container Apps and AKS.",
                    examWeight = "20 to 25 percent", componentId = "containers",
                    topics = Ai200Containers.topics + Ai200Containers2.topics, quiz = Ai200Containers2.quiz,
                ),
                Module(
                    id = "ai-data", number = 2, title = "AI data services",
                    summary = "Cosmos DB, PostgreSQL with pgvector, and Managed Redis: queries, vectors and RAG.",
                    examWeight = "30 to 35 percent", componentId = "aidata",
                    topics = Ai200Data.topics + Ai200Data2.topics + Ai200Data3.topics, quiz = Ai200Data3.quiz,
                ),
                Module(
                    id = "ai-connect", number = 3, title = "Connect and consume",
                    summary = "Service Bus, Event Grid and Azure Functions between the parts of an AI app.",
                    examWeight = "20 to 25 percent", componentId = "connect",
                    topics = Ai200Connect.topics, quiz = Ai200Connect.quiz,
                ),
                Module(
                    id = "ai-secure", number = 4, title = "Secure and monitor",
                    summary = "Key Vault, App Configuration, OpenTelemetry tracing and KQL.",
                    examWeight = "20 to 25 percent", componentId = "secure",
                    topics = Ai200Secure.topics + Ai200Secure2.topics, quiz = Ai200Secure2.quiz,
                ),
            ),
        )
    }
}
