package com.example.quizapp.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.quizapp.R
import com.example.quizapp.activites.Quiz

class Menu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val goToGalleryBtn = findViewById<Button>(R.id.goToGalleryBtn)
        val goToQuizBtn = findViewById<Button>(R.id.goToQuizBtn)

        goToGalleryBtn.setOnClickListener {
            val intent = Intent(this, Gallery::class.java)
            startActivity(intent)
        }

        goToQuizBtn.setOnClickListener {
            val intent = Intent(this, Quiz::class.java)
            startActivity(intent)
        }

    }
}