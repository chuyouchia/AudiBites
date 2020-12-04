package com.example.audibites

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val convertToPdfButton = findViewById<Button>(R.id.convert_pdf_button)

        convertToPdfButton.setOnClickListener {
            Toast.makeText(this, "You clicked me.",
                    Toast.LENGTH_SHORT).show()

            val intent = Intent(this, PDF2ImageActivity::class.java)
            startActivity(intent)
        }
    }

}
