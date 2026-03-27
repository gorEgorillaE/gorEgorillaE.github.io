package com.example.nasreels

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity

class SettingsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val ipInput: EditText = findViewById(R.id.ipInput)
        val countInput: EditText = findViewById(R.id.countInput)
        val saveButton: Button = findViewById(R.id.saveButton)

        ipInput.setText(AppSettings.getServerIp(this))
        countInput.setText(AppSettings.getVideoCount(this).toString())

        saveButton.setOnClickListener {
            val ip = ipInput.text.toString().trim()
            val count = countInput.text.toString().toIntOrNull()

            if (ip.isBlank() || count == null || count <= 0) {
                Toast.makeText(this, R.string.settings_invalid_input, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            AppSettings.setServerIp(this, ip)
            AppSettings.setVideoCount(this, count)
            Toast.makeText(this, R.string.settings_saved, Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
