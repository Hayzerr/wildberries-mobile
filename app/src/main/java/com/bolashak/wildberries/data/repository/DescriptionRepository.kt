package com.bolashak.wildberries.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DescriptionRepository @Inject constructor() {
    suspend fun getDescription(url: String): String = withContext(Dispatchers.IO) {
        try {
            if (url.isEmpty()) return@withContext "Description not available (Invalid URL)."
            
            val finalUrl = if (!url.startsWith("http")) "https:$url" else url

            val doc = Jsoup.connect(finalUrl)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                .timeout(5000)
                .get()
                
            var desc = doc.select(".product-page__description").text()
            
            if (desc.isBlank()) {
                desc = doc.select(".collapsible__content").text()
            }
            if (desc.isBlank()) {
                desc = doc.select("p[class*='description']").text()
            }
             if (desc.isBlank()) {
                desc = doc.select("meta[name='description']").attr("content")
            }

            desc.ifBlank { "Description could not be parsed from the page." }
        } catch (e: Exception) {
            e.printStackTrace()
            "Description not available (Offline or Error: ${e.message})."
        }
    }
}
