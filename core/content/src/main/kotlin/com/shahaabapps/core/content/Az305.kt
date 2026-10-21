package com.shahaabapps.core.content

/**
 * AZ-305 Designing Microsoft Azure Infrastructure Solutions, the Azure
 * Solutions Architect Expert exam, written for this app in plain English from
 * the official study guide (skills measured as of 17 April 2026) and
 * Microsoft Learn. It is a design exam: each topic is about choosing the right
 * service for a requirement, and why. The certification also needs AZ-104.
 *
 * Module order and weights follow the exam skills outline.
 */
object Az305 {
    /** Built once, as the other courses are: the content never changes at run time. */
    val course: Course by lazy {
        Course(
            id = "az-305",
            title = "AZ-305 Azure Solutions Architect",
            subtitle = "Four modules, one test each, then a mock exam.",
            goalId = "az-305",
            mockExam = Az305Mock.quiz,
            modules = listOf(
                Module(
                    id = "sa-identity", number = 1, title = "Identity, governance and monitoring",
                    summary = "Who may do what, how the estate is organised and kept compliant, and how you see it all.",
                    examWeight = "25 to 30 percent", componentId = "identity",
                    topics = Az305Identity.topics + Az305Identity2.topics, quiz = Az305Identity2.quiz,
                ),
                Module(
                    id = "sa-data", number = 2, title = "Data storage",
                    summary = "Relational, semi-structured and unstructured data, and moving and analysing it.",
                    examWeight = "20 to 25 percent", componentId = "data",
                    topics = Az305Data.topics, quiz = Az305Data.quiz,
                ),
                Module(
                    id = "sa-continuity", number = 3, title = "Business continuity",
                    summary = "Backup, disaster recovery and high availability that meet the recovery targets.",
                    examWeight = "15 to 20 percent", componentId = "continuity",
                    topics = Az305Continuity.topics, quiz = Az305Continuity.quiz,
                ),
                Module(
                    id = "sa-infra", number = 4, title = "Infrastructure",
                    summary = "Compute, application architecture, migrations and networks.",
                    examWeight = "30 to 35 percent", componentId = "infrastructure",
                    topics = Az305Infra.topics + Az305Infra2.topics + Az305Infra3.topics, quiz = Az305Infra3.quiz,
                ),
            ),
        )
    }
}
