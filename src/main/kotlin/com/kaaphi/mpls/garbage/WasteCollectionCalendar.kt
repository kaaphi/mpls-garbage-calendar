package com.kaaphi.mpls.garbage

import biweekly.ICalendar
import com.apptastic.rssreader.RssReader
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

class WasteCollectionCalendar(val url : String, val ttlMillis: Long = TimeUnit.DAYS.toMillis(1)) {
    private val log = LoggerFactory.getLogger(WasteCollectionCalendar::class.java)

    private val ttlNanos = TimeUnit.MILLISECONDS.toNanos(ttlMillis)
    private var lastRetrieveTime : Long = 0 //not used until calendar is initialized
    private lateinit var calendar : ICalendar

    @Synchronized
    fun get() : ICalendar {
        if(shouldReloadCalendar()) {
            log.debug("Reloading calendar {}", url)
            calendar = WasteCollectionEventConversions.rss2iCal(RssReader().read(url))
            lastRetrieveTime = System.nanoTime()
        }

        return calendar
    }

    private fun shouldReloadCalendar() : Boolean {
        return !this::calendar.isInitialized
            ||
        System.nanoTime() - lastRetrieveTime > ttlNanos
    }
}