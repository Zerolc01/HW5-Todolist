package com.lc.todolist.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lc.todolist.NoteOperator
import com.lc.todolist.R
import com.lc.todolist.beans.Note
import java.util.ArrayList


class NoteListAdapter(operator: NoteOperator) :
    RecyclerView.Adapter<NoteViewHolder?>() {
    private val operator: NoteOperator = operator
    private val notes: MutableList<Note> = ArrayList<Note>()
    fun refresh(newNotes: List<Note>?) {
        notes.clear()
        if (newNotes != null) {
            notes.addAll(newNotes)
        }
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, pos: Int): NoteViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item, parent, false)
        return NoteViewHolder(itemView, operator)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, pos: Int) {
        holder.bind(notes[pos])
    }

    override fun getItemCount(): Int = notes.size

}