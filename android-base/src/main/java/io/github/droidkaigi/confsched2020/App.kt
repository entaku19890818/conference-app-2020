package io.github.droidkaigi.confsched2020

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.BuildCompat
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import io.github.droidkaigi.confsched2020.announcement.ui.subscribeAnnouncementTopic
import io.github.droidkaigi.confsched2020.di.AppComponent
import io.github.droidkaigi.confsched2020.di.AppComponentHolder
import io.github.droidkaigi.confsched2020.di.createAppComponent
import io.github.droidkaigi.confsched2020.ext.changedForever
import io.github.droidkaigi.confsched2020.model.SystemProperty
import io.github.droidkaigi.confsched2020.system.store.SystemStore
import timber.log.LogcatTree
import timber.log.Timber
import javax.inject.Inject

open class App : DaggerApplication(), AppComponentHolder {
    @Inject
    lateinit var systemStore: SystemStore

    override val appComponent: AppComponent by lazy {
        createAppComponent()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return appComponent
    }

    override fun onCreate() {
        super.onCreate()
        setupTimber()
        setupFirestore()
        setupNightMode()
        systemStore.systemProperty.changedForever {
            subscribeAnnouncementTopic(it)
        }
    }

    open fun subscribeAnnouncementTopic(systemProperty: SystemProperty) {
        subscribeAnnouncementTopic(systemProperty.lang)
    }
    private fun setupTimber() {
        Timber.plant(LogcatTree())
    }

    private fun setupFirestore() {
        FirebaseApp.initializeApp(this)
        val firestore = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()
        firestore.firestoreSettings = settings
    }

    private fun setupNightMode() {
        val nightMode = if (BuildCompat.isAtLeastQ()) {
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        } else {
            AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
        }
        AppCompatDelegate.setDefaultNightMode(nightMode)
    }
}