package com.example.crudsqlite

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class NotesAdapter(private var notes: List<Note>, context: Context) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {
    private val db: NotesDatabaseHelper = NotesDatabaseHelper(context)

    class NoteViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val titleTextView: TextView = itemview.findViewById(R.id.titleTextView)
        val contentTextView: TextView = itemview.findViewById(R.id.contentTextView)
        val updateButton: ImageView = itemview.findViewById(R.id.updateButton)
        val deleteButton: ImageView = itemview.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int = notes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {

        val note = notes[position]
        holder.titleTextView.text = note.title
        holder.contentTextView.text = note.content

        holder.updateButton.setOnClickListener {
            val intent = Intent(holder.itemView.context, UpdataActivity::class.java).apply {
                putExtra("note_id", note.id)
            }
            holder.itemView.context.startActivity(intent)
        }

        holder.deleteButton.setOnClickListener {
            showDeleteAlertDialog(holder.itemView.context, note)
        }
    }

    private fun showDeleteAlertDialog(context: Context?, note: Note) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle("Delete Data")
        alertDialogBuilder.setMessage("Are you sure you want to delete this Data?")

        alertDialogBuilder.setPositiveButton("Yes") { _, _ ->
            db.deleteNote(note.id)
            refreshData(db.getAllNotes())
            Toast.makeText(context, "Deleted Date", Toast.LENGTH_SHORT).show()
        }

        alertDialogBuilder.setNegativeButton("No") { _, _ ->

        }

        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()
        alertDialog.setCancelable(false)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refreshData(newNotes: List<Note>) {
        notes = newNotes
        notifyDataSetChanged()
    }
}
