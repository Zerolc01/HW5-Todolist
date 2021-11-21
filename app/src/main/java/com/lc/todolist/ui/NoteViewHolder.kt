package com.lc.todolist.ui


import android.graphics.Color
import android.graphics.Paint
import android.view.View
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lc.todolist.NoteOperator
import com.lc.todolist.R
import com.lc.todolist.beans.Note
import com.lc.todolist.beans.State
import java.text.SimpleDateFormat
import java.util.*


class NoteViewHolder(itemView: View, operator: NoteOperator) :
    RecyclerView.ViewHolder(itemView) {
    private val operator: NoteOperator = operator
    private val checkBox: CheckBox = itemView.findViewById(R.id.checkbox)
    private val contentText: TextView = itemView.findViewById(R.id.text_content)
    private val dateText: TextView = itemView.findViewById(R.id.text_date)
    private val deleteBtn: View = itemView.findViewById(R.id.btn_cancel)


    fun bind(note: Note) {
        contentText.text = note.getContent()
        dateText.text = SIMPLE_DATE_FORMAT.format(note.getDate())
        checkBox.setOnCheckedChangeListener(null)
        checkBox.isChecked = (note.getState() == State.DONE)
        checkBox.setOnCheckedChangeListener { _ , isChecked ->
            note.setState(if (isChecked) State.DONE else State.TODO)
            operator.updateNote(note)
        }

        deleteBtn.setOnClickListener {
            operator.deleteNote(note)
        }


        if (note.getState() == State.DONE) {
            contentText.setTextColor(Color.GRAY)
            contentText.paintFlags = contentText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            contentText.setTextColor(Color.BLACK)
            contentText.paintFlags = contentText.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
        note.getPriority()?.let { itemView.setBackgroundColor(it.color) }
    }

    companion object {
        private val SIMPLE_DATE_FORMAT =
            SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.ENGLISH)
    }


}

