package com.example.crudsqlite

import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.crudsqlite.databinding.ActivityAddNoteBinding

class AddNoteActivity : AppCompatActivity() {
    private lateinit var binder: ActivityAddNoteBinding
    private lateinit var db:NotesDatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder=ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binder.root)

        db= NotesDatabaseHelper(this)

        binder.saveButton.setOnClickListener {
            val title=binder.titleEditText.text.toString()
            val content=binder.contentEditText.text.toString()
            val note=Note(0,title,content)
            db.inserNote(note)
            finish()
            Toast.makeText(this, "SAVE DATA", Toast.LENGTH_SHORT).show()
        }
    }
}