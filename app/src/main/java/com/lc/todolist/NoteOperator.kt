package com.lc.todolist

import com.lc.todolist.beans.Note




interface NoteOperator {
    fun deleteNote(note: Note)
    fun updateNote(note: Note)
}
