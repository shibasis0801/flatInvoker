package dev.shibasis.reaktor.io.adapters

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import okio.Path.Companion.toPath

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dev.shibasis.reaktor.db.adapters.KeyValueStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

// todo de-dupe
class JetpackKeyValueStore(
    name: String,
    fileAdapter: FileAdapter<*>
): KeyValueStore<Unit>(Unit, name) {
    val dataStore = PreferenceDataStoreFactory.createWithPath(
        corruptionHandler = null,
        migrations = emptyList(),
        produceFile = { "${fileAdapter.cacheDirectory}/${name}.preferences_pb".toPath() }
    )

    override suspend fun get(key: String): String? {
        val dataKey = stringPreferencesKey(key)
        return dataStore.data.map { it[dataKey] }.first()
    }

    override suspend fun set(key: String, value: String) {
        val dataKey = stringPreferencesKey(key)
        dataStore.edit { preferences -> preferences[dataKey] = value }
    }

    override suspend fun remove(key: String) {
        val dataKey = stringPreferencesKey(key)
        dataStore.edit { preferences -> preferences.remove(dataKey) }
    }

    override suspend fun clear() {
        dataStore.edit { preferences -> preferences.clear() }
    }
}