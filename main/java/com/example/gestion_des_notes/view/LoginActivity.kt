package com.example.gestion_des_notes.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gestion_des_notes.R
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {

    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button
    private lateinit var forgotPasswordText: TextView
    private lateinit var usernameLayout: TextInputLayout
    private lateinit var passwordLayout: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Vérifier si l'utilisateur est déjà connecté
        val prefs = getSharedPreferences("NotesApp", MODE_PRIVATE)
        if (prefs.getBoolean("isLoggedIn", false)) {
            // Aller directement à MainActivity si déjà connecté
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_login)

        // Initialiser les vues
        usernameLayout = findViewById(R.id.usernameLayout)
        passwordLayout = findViewById(R.id.passwordLayout)
        usernameInput = findViewById(R.id.usernameInput)
        passwordInput = findViewById(R.id.passwordInput)
        loginButton = findViewById(R.id.loginButton)
        forgotPasswordText = findViewById(R.id.forgotPasswordText)

        // Gérer le clic sur le bouton de connexion
        loginButton.setOnClickListener {
            // Réinitialiser les erreurs
            usernameLayout.error = null
            passwordLayout.error = null

            val username = usernameInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            // Validation simple
            var hasError = false

            if (username.isEmpty()) {
                usernameLayout.error = "Nom d'utilisateur requis"
                hasError = true
            }

            if (password.isEmpty()) {
                passwordLayout.error = "Mot de passe requis"
                hasError = true
            }

            if (hasError) return@setOnClickListener

            // En pratique, vous feriez une vraie authentification ici
            // Pour cette démo, on accepte admin/admin
            if (username == "admin" && password == "admin") {
                // Marquer comme connecté
                prefs.edit().putBoolean("isLoggedIn", true).apply()

                // Aller à MainActivity
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Identifiants incorrects", Toast.LENGTH_SHORT).show()
            }
        }

        // Gérer le clic sur "Mot de passe oublié"
        forgotPasswordText.setOnClickListener {
            Toast.makeText(this, "Contactez l'administrateur", Toast.LENGTH_SHORT).show()
        }
    }
}