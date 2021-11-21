package com.lc.todolist.beans

import java.util.*

class Note(id: Long) {

    var id: Long = 0
    private var date: Date? = null
    private var state: State? = null
    private var content: String? = null
    private var priority: Priority? = null

    fun Note(id: Long) {    this.id = id    }

    fun getDate(): Date? {    return date    }

    fun setDate(date: Date?) {    this.date = date    }

    fun getState(): State? {    return state    }

    fun setState(state: State?) {    this.state = state    }

    fun getContent(): String? {    return content    }

    fun setContent(content: String?) {    this.content = content    }

    fun getPriority(): Priority? {    return priority    }

    fun setPriority(priority: Priority?) {    this.priority = priority    }
}