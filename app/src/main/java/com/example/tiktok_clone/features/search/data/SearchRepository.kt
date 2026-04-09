package com.example.tiktok_clone.features.search.data

import android.content.SharedPreferences
import com.example.tiktok_clone.features.search.api.SearchApiService
import com.example.tiktok_clone.features.search.model.DiscoverResponse
import com.example.tiktok_clone.features.search.model.SearchResponse

class SearchRepository(
    private val api: SearchApiService,
    private val prefs: SharedPreferences,
) {
    private val recentKey = "search_recent_queries"

    fun getRecentSearches(): List<String> {
        val raw = prefs.getString(recentKey, null) ?: return emptyList()
        return raw.lines().map { it.trim() }.filter { it.isNotEmpty() }
    }

    fun addRecentSearch(query: String) {
        val q = query.trim().ifEmpty { return }
        val next = listOf(q) + getRecentSearches().filterNot { it.equals(q, ignoreCase = true) }
        prefs.edit().putString(recentKey, next.take(30).joinToString("\n")).apply()
    }

    fun removeRecentSearch(query: String) {
        val next = getRecentSearches().filterNot { it == query }
        prefs.edit().putString(recentKey, next.joinToString("\n")).apply()
    }

    suspend fun suggest(query: String): List<String> = api.suggest(query)

    suspend fun discover(): DiscoverResponse = api.discover()

    suspend fun searchAll(query: String): SearchResponse = api.searchAll(query)
}
