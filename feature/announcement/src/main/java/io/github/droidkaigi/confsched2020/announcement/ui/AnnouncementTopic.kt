package io.github.droidkaigi.confsched2020.announcement.ui

import io.github.droidkaigi.confsched2020.model.Lang
import io.github.droidkaigi.confsched2020.model.defaultLang
import io.github.droidkaigi.confsched2020.notification.Topic
import io.github.droidkaigi.confsched2020.notification.worker.ManageTopicSubscriptionWorker

fun subscribeAnnouncementTopic(lang: Lang = defaultLang()) {
    val allAnnouncementTopics = arrayOf(Topic.JaAnnouncement, Topic.EnAnnouncement)

    val subscribeTopic = when (lang) {
        Lang.EN -> Topic.EnAnnouncement
        Lang.JA -> Topic.JaAnnouncement
    }

    ManageTopicSubscriptionWorker.start(
        subscribes = listOf(subscribeTopic),
        unsubscribes = allAnnouncementTopics.filter { it != subscribeTopic }
    )
}
