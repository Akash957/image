package com.example.imagesqlite

import DatabaseHelper
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)
        imageView = findViewById(R.id.imageView)

        // Set a placeholder image initially
        imageView.setImageResource(R.drawable.images)

        val saveImageButton: Button = findViewById(R.id.saveImageButton)
        saveImageButton.setOnClickListener {
            // Example: Save Bitmap to SQLite and update ImageView
            val bitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.images)
            saveImageToDatabase(bitmap)
        }
    }

    private fun saveImageToDatabase(bitmap: Bitmap) {
        GlobalScope.launch(Dispatchers.IO) {
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val imageInBytes = byteArrayOutputStream.toByteArray()

            val db = dbHelper.writableDatabase
            val contentValues = ContentValues()
            contentValues.put(DatabaseHelper.COLUMN_IMAGE, imageInBytes)

            val result = db.insert(DatabaseHelper.TABLE_NAME, null, contentValues)

            if (result != -1L) {
                showToast("Image saved successfully")
                // Update ImageView with the saved image
                runOnUiThread {
                    imageView.setImageBitmap(bitmap)
                }
            } else {
                showToast("Error saving image")
            }

            db.close()
        }
    }

    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
        }
    }
}
