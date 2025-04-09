package com.example.gestion_des_notes.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.gestion_des_notes.R
import com.example.gestion_des_notes.model.Etudiant
import com.example.gestion_des_notes.model.Matiere
import com.example.gestion_des_notes.model.Note
import com.google.android.material.textfield.TextInputLayout

class AjouterNoteActivity : AppCompatActivity() {

    private lateinit var inputNom: EditText
    private lateinit var inputPrenom: EditText
    private lateinit var inputEmail: EditText
    private lateinit var inputMatiere: EditText
    private lateinit var inputCoeff: EditText
    private lateinit var inputNote: EditText
    private lateinit var btnSave: Button

    private lateinit var inputNomLayout: TextInputLayout
    private lateinit var inputPrenomLayout: TextInputLayout
    private lateinit var inputEmailLayout: TextInputLayout
    private lateinit var inputMatiereLayout: TextInputLayout
    private lateinit var inputCoeffLayout: TextInputLayout
    private lateinit var inputNoteLayout: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ajouter_note)

        // Configurer la toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Ajouter une note"

        toolbar.setNavigationOnClickListener {
            finish()
        }

        // Initialiser les vues
        inputNomLayout = findViewById(R.id.inputNomLayout)
        inputPrenomLayout = findViewById(R.id.inputPrenomLayout)
        inputEmailLayout = findViewById(R.id.inputEmailLayout)
        inputMatiereLayout = findViewById(R.id.inputMatiereLayout)
        inputCoeffLayout = findViewById(R.id.inputCoeffLayout)
        inputNoteLayout = findViewById(R.id.inputNoteLayout)

        inputNom = findViewById(R.id.inputNom)
        inputPrenom = findViewById(R.id.inputPrenom)
        inputEmail = findViewById(R.id.inputEmail)
        inputMatiere = findViewById(R.id.inputMatiere)
        inputCoeff = findViewById(R.id.inputCoeff)
        inputNote = findViewById(R.id.inputNote)
        btnSave = findViewById(R.id.btnSave)

        btnSave.setOnClickListener {
            // Réinitialiser les erreurs
            inputNomLayout.error = null
            inputPrenomLayout.error = null
            inputEmailLayout.error = null
            inputMatiereLayout.error = null
            inputCoeffLayout.error = null
            inputNoteLayout.error = null

            // Validation des champs
            var hasError = false

            if (inputNom.text.toString().isEmpty()) {
                inputNomLayout.error = "Le nom est requis"
                hasError = true
            }

            if (inputPrenom.text.toString().isEmpty()) {
                inputPrenomLayout.error = "Le prénom est requis"
                hasError = true
            }

            if (inputEmail.text.toString().isEmpty()) {
                inputEmailLayout.error = "L'email est requis"
                hasError = true
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(inputEmail.text.toString()).matches()) {
                inputEmailLayout.error = "Email invalide"
                hasError = true
            }

            if (inputMatiere.text.toString().isEmpty()) {
                inputMatiereLayout.error = "La matière est requise"
                hasError = true
            }

            // Validation du coefficient (positif)
            val coeff = inputCoeff.text.toString().toIntOrNull()
            if (coeff == null || coeff <= 0) {
                inputCoeffLayout.error = "Le coefficient doit être un nombre positif"
                hasError = true
            }

            // Validation de la note (entre 0 et 20)
            val noteValue = inputNote.text.toString().toFloatOrNull()
            if (noteValue == null) {
                inputNoteLayout.error = "La note doit être un nombre"
                hasError = true
            } else if (noteValue < 0 || noteValue > 20) {
                inputNoteLayout.error = "La note doit être entre 0 et 20"
                hasError = true
            }

            if (hasError) return@setOnClickListener

            val etudiant = Etudiant(
                inputNom.text.toString(),
                inputPrenom.text.toString(),
                inputEmail.text.toString()
            )
            val matiere = Matiere(
                inputMatiere.text.toString(),
                coeff!!
            )

            val newNote = Note(etudiant, matiere, noteValue!!)

            val intent = Intent()
            intent.putExtra("newNote", newNote)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}