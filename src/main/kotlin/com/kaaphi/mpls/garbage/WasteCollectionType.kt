package com.kaaphi.mpls.garbage

enum class WasteCollectionType() {
    RECYCLING,
    GARBAGE,
    OTHER {
        override fun matches(title : String) = false
    };

    fun getLabel() = name.substring(0,1) + name.toLowerCase().substring(1)
    open fun matches(title : String) = title.contains(name, true)
}

fun parseWasteCollectionType(value : String) : WasteCollectionType {
    return WasteCollectionType.values().find { it.matches(value) } ?: WasteCollectionType.OTHER
}