package com.example.carcheck.ui.composables

fun TranslateNodeCategoryToRussian(category: String): String {
    return when (category) {
        "ENGINE" -> "Двигатель"
        "SUSPENSION_FRONT" -> "Передняя подвеска"
        "SUSPENSION_REAR" -> "Задняя подвеска"
        "BRAKES" -> "Тормозная система"
        "ELECTRONICS" -> "Электроника"
        "BODY" -> "Кузов"
        "TRUNK" -> "Багажник"
        "FUEL_SYSTEM" -> "Топливная система"
        "CABIN" -> "Салон"
        "COUPLING" -> "Сцепление"
        "TRANSMISSION" -> "Трансмиссия"
        else -> "Неизвестная категория"
    }
}
