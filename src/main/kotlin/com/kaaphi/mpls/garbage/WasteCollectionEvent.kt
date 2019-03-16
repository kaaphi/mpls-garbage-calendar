package com.kaaphi.mpls.garbage

import java.time.LocalDate

data class WasteCollectionEvent(val day : LocalDate, val type : WasteCollectionType) : Comparable<WasteCollectionEvent> {
    override fun compareTo(other: WasteCollectionEvent) =
        compareValuesBy(this, other, {it.day}, {it.type})
}