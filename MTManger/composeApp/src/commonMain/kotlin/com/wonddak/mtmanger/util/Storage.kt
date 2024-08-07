package com.wonddak.mtmanger.util

import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okio.Path.Companion.toPath

internal const val MT_MANAGER_PREFERENCES = "mt_preferences.preferences_pb"

class Storage(provider: DataStoreProvider) {
    private val dataStore = provider.getDataStore()

    val id: Flow<Int>
        get() = dataStore.data.map {
            it[intPreferencesKey("id")] ?: 0
        }

    suspend fun updateId(id: Int) {
        dataStore.edit {
            it[intPreferencesKey("id")] = id
        }
    }

    val isFirst: Flow<Boolean>
        get() = dataStore.data.map {
            it[booleanPreferencesKey("first")] ?: true
        }

    suspend fun clearFirst() {
        dataStore.edit {
            it[booleanPreferencesKey("first")] = false
        }
    }
}

internal fun createDataStoreWithDefaults(
    migrations: List<DataMigration<Preferences>> = emptyList(),
    producePath: () -> String,
) = PreferenceDataStoreFactory
    .createWithPath(
        corruptionHandler = null,
        migrations = migrations,
        produceFile = {
            producePath().toPath()
        }
    )

expect class DataStoreProvider() {
    fun getDataStore(): DataStore<Preferences>
}