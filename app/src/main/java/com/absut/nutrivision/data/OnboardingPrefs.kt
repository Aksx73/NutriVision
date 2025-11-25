package com.absut.nutrivision.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val DATASTORE_NAME = "onboarding_prefs"

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(DATASTORE_NAME)

object OnboardingPrefs {
    private val KEY_DONE = booleanPreferencesKey("onboarding_done")

    fun isOnboardingDoneFlow(context: Context): Flow<Boolean?> =
        context.dataStore.data.map { prefs: Preferences ->
            prefs[KEY_DONE]
        }

    suspend fun setOnboardingDone(context: Context, done: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_DONE] = done
        }
    }
}
