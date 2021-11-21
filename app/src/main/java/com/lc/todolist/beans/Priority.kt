package com.lc.todolist.beans

import android.graphics.Color


enum class Priority(val intValue: Int, val color: Int) {
    High(2, Color.RED), Medium(1, Color.YELLOW), Low(0, Color.GREEN);

    companion object {
        fun from(intValue: Int): Priority {
            for (priority in values()) {
                if (priority.intValue == intValue) {
                    return priority
                }
            }
            return Low // default
        }
    }
}