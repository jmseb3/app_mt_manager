package com.wonddak.mtmanger.util

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSFileManager
import platform.Foundation.NSLibraryDirectory
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

actual class DataStoreProvider actual constructor() {
    private val dataStore = createDataStoreWithDefaults() {
        providePath()
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun providePath(): String {
        val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
            directory = NSLibraryDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
        )
        return requireNotNull(documentDirectory).path + "/" + MT_MANAGER_PREFERENCES
    }

    actual fun getDataStore(): DataStore<Preferences> {
        return dataStore
    }
}