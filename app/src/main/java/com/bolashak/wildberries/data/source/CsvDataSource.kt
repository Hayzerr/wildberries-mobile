package com.bolashak.wildberries.data.source

import android.content.Context
import com.bolashak.wildberries.data.model.ProductDto
import com.opencsv.bean.CsvToBeanBuilder
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.InputStreamReader
import javax.inject.Inject

class CsvDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun getAllProducts(): List<ProductDto> {
        return try {
            val assetManager = context.assets
            val inputStream = assetManager.open("wb_all_categories.csv")
            val reader = InputStreamReader(inputStream)
            
            val beans = CsvToBeanBuilder<ProductDto>(reader)
                .withType(ProductDto::class.java)
                .withIgnoreLeadingWhiteSpace(true)
                .build()
                .parse()
                
            beans
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
