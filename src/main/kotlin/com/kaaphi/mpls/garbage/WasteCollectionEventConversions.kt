package com.kaaphi.mpls.garbage

import biweekly.ICalendar
import biweekly.component.VEvent
import com.apptastic.rssreader.Item
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.stream.Collectors
import java.util.stream.Stream

object WasteCollectionEventConversions {
    private val PUB_DATE_FORMAT = DateTimeFormatter.ofPattern("[EEE, ]d MMM yyyy HH:mm[:ss] z");

    fun rss2wasteCollectionEvent(item : Item) : WasteCollectionEvent {
        val type = item.title.map(::parseWasteCollectionType).orElse(WasteCollectionType.OTHER)
        val date = item.pubDate.map(PUB_DATE_FORMAT::parse).map(LocalDate::from).get()
        return WasteCollectionEvent(date, type)
    }

    fun rss2iCal(items : Stream<Item>) : ICalendar {
        val iCal = ICalendar()

        items.map(this::rss2wasteCollectionEvent)
                .filter{it.type != WasteCollectionType.OTHER}
                .collect(Collectors.toList())
                .groupBy(WasteCollectionEvent::day)
                .forEach{ (date, events) ->
                    val event : VEvent = VEvent()
                    event.setSummary(events
                            .sorted()
                            .map(WasteCollectionEvent::type)
                            .map(WasteCollectionType::getLabel)
                            .joinToString(", "))
                    event.setDateStart(localDateToDate(date), false)
                    event.setDateEnd(localDateToDate(date.plusDays(1)), false)
                    iCal.addEvent(event)
                }

        return iCal
    }

    private fun localDateToDate(localDate : LocalDate) : Date {
        //the Biweekly library assumes that all dates with no time component are created using the JVM default timezone
        val instant = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()

        return Date.from(instant)
    }
}