package com.lc.todolist

import android.app.Activity
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatRadioButton
import com.lc.todolist.beans.Priority
import com.lc.todolist.beans.State
import com.lc.todolist.db.TodoContract
import com.lc.todolist.db.TodoDbHelper

class TodoActivity : AppCompatActivity() {
    private var editText: EditText? = null
    private var addBtn: Button? = null
    private var radioGroup: RadioGroup? = null
    private var lowRadio: AppCompatRadioButton? = null

    private var dbHelper: TodoDbHelper? = null
    private var database: SQLiteDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo)
        setTitle("Take a Note")
        dbHelper = TodoDbHelper(this)

        database = dbHelper?.writableDatabase

        editText = findViewById(R.id.edit_text)
        editText?.isFocusable = true
        editText?.requestFocus()

        val inputManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager?.showSoftInput(editText, 0)

        radioGroup = findViewById(R.id.radio_group)
        lowRadio = findViewById(R.id.btn_low)
        lowRadio?.isChecked = true

        addBtn = findViewById(R.id.btn_add)
        addBtn?.setOnClickListener(View.OnClickListener {
            val content: CharSequence = editText?.text.toString()
            if (TextUtils.isEmpty(content)) {
                Toast.makeText(
                    this@TodoActivity,
                    "No content to add", Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener
            }
            val succeed = saveNote2Database(
                content.toString().trim { it <= ' ' },
                getSelectedPriority()
            )
            if (succeed) {
                Toast.makeText(
                    this@TodoActivity,
                    "Note added", Toast.LENGTH_SHORT
                ).show()
                setResult(RESULT_OK)
            } else {
                Toast.makeText(
                    this@TodoActivity,
                    "Error", Toast.LENGTH_SHORT
                ).show()
            }
            finish()
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        database!!.close()
        database = null
        dbHelper?.close()
        dbHelper = null
    }

    private fun saveNote2Database(content: String, priority: Priority): Boolean {
        if (database == null || TextUtils.isEmpty(content)) {
            return false
        }
        val values = ContentValues()
        values.put(TodoContract.TodoNote.COLUMN_CONTENT, content)
        values.put(TodoContract.TodoNote.COLUMN_STATE, State.TODO.intValue)
        values.put(TodoContract.TodoNote.COLUMN_DATE, System.currentTimeMillis())
        values.put(TodoContract.TodoNote.COLUMN_PRIORITY, priority.intValue)
        val rowId = database!!.insert(TodoContract.TodoNote.TABLE_NAME, null, values)
        return rowId != -1L
    }

    private fun getSelectedPriority(): Priority {
        return when (radioGroup!!.checkedRadioButtonId) {
            R.id.btn_high -> Priority.High
            R.id.btn_medium -> Priority.Medium
            else -> Priority.Low
        }
    }
}