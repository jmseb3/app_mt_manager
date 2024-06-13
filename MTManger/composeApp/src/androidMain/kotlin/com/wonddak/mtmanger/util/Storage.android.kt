package com.wonddak.mtmanger.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.Preferences
import org.koin.java.KoinJavaComponent

actual class DataStoreProvider actual constructor() {
    private val context: Context = KoinJavaComponent.getKoin().get()

    private val dataStore = createDataStoreWithDefaults(
        listOf(SharedPreferencesMigration(context, "mainMT"))
    ) {
        context.filesDir.resolve(MT_MANAGER_PREFERENCES).absolutePath
    }

    actual fun getDataStore(): DataStore<Preferences> {
        return dataStore
    }
}