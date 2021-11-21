package com.lc.todolist

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.BaseColumns
import android.widget.Button
import androidx.annotation.Nullable
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.lc.todolist.beans.Note
import com.lc.todolist.beans.Priority
import com.lc.todolist.beans.State
import com.lc.todolist.db.TodoDbHelper
import com.lc.todolist.db.TodoContract
import com.lc.todolist.ui.NoteListAdapter
import com.lc.todolist.NoteOperator
import java.util.*

class MainActivity : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null
    private var notesAdapter: NoteListAdapter? = null

    private var dbHelper: TodoDbHelper? = null
    private var database: SQLiteDatabase? = null

//    private var canBtn : Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fab: FloatingActionButton = findViewById(R.id.fab)

        fab.setOnClickListener{
            startActivityForResult(
                Intent(this@MainActivity, TodoActivity::class.java),
                REQUEST_CODE_ADD
            )
        }

        dbHelper = TodoDbHelper(this)
        database = dbHelper!!.writableDatabase
        recyclerView = findViewById(R.id.recycle_list)

        recyclerView?.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL, false
        )
        recyclerView?.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )


        notesAdapter = NoteListAdapter(
            object : NoteOperator {
            override fun deleteNote(note: Note) {
//                if (note != null) {
                    this@MainActivity.deleteNote(note)
//                }
            }

            override fun updateNote(note: Note) {
//                if (note != null) {
                    this@MainActivity.updateNode(note)
//                }
            }
        }
        )

        recyclerView?.adapter = notesAdapter

        notesAdapter?.refresh(loadNotesFromDatabase())
    }

    override fun onDestroy() {
        super.onDestroy()
        database!!.close()
        database = null
        dbHelper?.close()
        dbHelper = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ADD
            && resultCode == RESULT_OK
        ) {
            notesAdapter?.refresh(loadNotesFromDatabase())
        }
    }

    @SuppressLint("Range")
    private fun loadNotesFromDatabase(): List<Note> {
        if (database == null) {
            return emptyList()
        }
        val result: MutableList<Note> = LinkedList<Note>()
        var cursor: Cursor? = null
        try {
            cursor = database!!.query(
                TodoContract.TodoNote.TABLE_NAME, null,
                null, null,
                null, null, TodoContract.TodoNote.COLUMN_PRIORITY + " DESC"
            )
            while (cursor.moveToNext()) {
                val id : Long = cursor.getLong(cursor.getColumnIndex(TodoContract.TodoNote.ID))
                val content = cursor.getString(cursor.getColumnIndex(TodoContract.TodoNote.COLUMN_CONTENT))
                val dateMs = cursor.getLong(cursor.getColumnIndex(TodoContract.TodoNote.COLUMN_DATE))
                val intState = cursor.getInt(cursor.getColumnIndex(TodoContract.TodoNote.COLUMN_STATE))
                val intPriority = cursor.getInt(cursor.getColumnIndex(TodoContract.TodoNote.COLUMN_PRIORITY))
                val note = Note(id)
                note.setContent(content)
                note.setDate(Date(dateMs))
                note.setState(State.from(intState))
                note.setPriority(Priority.from(intPriority))
                result.add(note)
            }
        } finally {
            cursor?.close()
        }
        return result
    }

    private fun deleteNote(note: Note) {
        if (database == null) {
            return
        }
        val rows = database!!.delete(
            TodoContract.TodoNote.TABLE_NAME, TodoContract.TodoNote.ID + "=?", arrayOf(
                note?.id.toString()
            )
        )
        if (rows > 0) {
            notesAdapter?.refresh(loadNotesFromDatabase())
        }
    }

    private fun updateNode(note: Note) {
        if (database == null) {
            return
        }
        val values = ContentValues()
        values.put(TodoContract.TodoNote.COLUMN_STATE, note.getState()?.intValue)
        val rows = database!!.update(
            TodoContract.TodoNote.TABLE_NAME, values, TodoContract.TodoNote.ID + "=?", arrayOf(
                note?.id.toString()
            )
        )
        if (rows > 0) {
            notesAdapter?.refresh(loadNotesFromDatabase())
        }
    }

    companion object {
        private const val REQUEST_CODE_ADD = 1002
    }
}
