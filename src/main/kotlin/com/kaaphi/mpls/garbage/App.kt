package com.kaaphi.mpls.garbage

import biweekly.Biweekly
import io.javalin.Javalin
import org.slf4j.LoggerFactory
import java.io.File
import java.util.*

class App(private val port : Int, private val calendarConfig : Map<String,String>) {
    private val log = LoggerFactory.getLogger(App::class.java)

    private val app = Javalin.create().defaultContentType("text/calendar")

    init {
        calendarConfig.forEach {(path, url) ->
            log.debug("Mapping {} to URL {}", path, url)
            val cal = WasteCollectionCalendar(url)

            app.get(path) { ctx ->
                ctx.result(Biweekly.write(cal.get()).go())
            }
        }
    }

    fun start() {
        app.start(port)
    }
}

fun main(args: Array<String>) {
    val config = mutableMapOf<String,String>()

    System.getProperty("config")?.let {
        File(it).forEachLine {
            val split = it.split("=",limit = 2)
            config[split[0]] = split[1]
        }
    }

    config.putAll(args.associate {
        val split = it.split("=",limit = 2)
        Pair(split[0], split[1])
    })

    System.out.println("Starting with args $config")
    App(7000, config).start()
}
