package com.bolashak.wildberries.domain.model

enum class CategoryType(val id: String, val displayName: String) {
    SMARTPHONES("Smartphones", "Смартфоны"),
    LAPTOPS("Laptops", "Ноутбуки"),
    TABLETS("Tablets", "Планшеты"),
    HEADPHONES("Headphones", "Наушники"),
    SMART_WATCHES("Smart Watches", "Умные часы"),
    TSHIRTS("T-Shirts", "Футболки"),
    JEANS("Jeans", "Джинсы"),
    DRESSES("Dresses", "Платья"),
    JACKETS("Jackets", "Куртки"),
    SNEAKERS("Sneakers", "Кроссовки"),
    BOOTS("Boots", "Ботинки"),
    BACKPACKS("Backpacks", "Рюкзаки"),
    WATCHES("Watches", "Часы"),
    COSMETICS("Cosmetics", "Косметика"),
    PERFUMES("Perfumes", "Парфюм"),
    BEDDING("Bedding", "Постельное"),
    KITCHENWARE("Kitchenware", "Посуда"),
    SPORTSWEAR("Sportswear", "Спорт"),
    TOYS("Toys", "Игрушки"),
    BOOKS("Books", "Книги");

    companion object {
        fun fromId(id: String): CategoryType? = entries.find { it.id.equals(id, ignoreCase = true) }
    }
}

data class Category(
    val type: CategoryType,
    val count: Int = 0
)
