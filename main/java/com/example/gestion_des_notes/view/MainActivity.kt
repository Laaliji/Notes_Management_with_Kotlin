package com.example.gestion_des_notes.view

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gestion_des_notes.R
import com.example.gestion_des_notes.adapter.NoteAdapter
import com.example.gestion_des_notes.model.Etudiant
import com.example.gestion_des_notes.model.Matiere
import com.example.gestion_des_notes.model.Note
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var searchInput: EditText
    private lateinit var statsTextView: TextView
    private val notes = mutableListOf<Note>()

    companion object {
        private const val ADD_NOTE_REQUEST_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configurer la toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Gestion des Notes"

        // Initialiser les vues
        recyclerView = findViewById(R.id.recyclerView)
        searchInput = findViewById(R.id.searchInput)
        statsTextView = findViewById(R.id.statsTextView)

        // FAB pour ajouter une note
        val addNoteFab = findViewById<FloatingActionButton>(R.id.addNoteFab)

        // Configurer le RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        noteAdapter = NoteAdapter(notes) { position ->
            showDeleteConfirmation(position)
        }
        recyclerView.adapter = noteAdapter

        // Bouton pour ajouter une note
        addNoteFab.setOnClickListener {
            val intent = Intent(this, AjouterNoteActivity::class.java)
            startActivityForResult(intent, ADD_NOTE_REQUEST_CODE)
        }

        // Recherche
        searchInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterNotes(s.toString())
            }
        })

        // Boutons de tri
        findViewById<Button>(R.id.btnSortByName).setOnClickListener {
            notes.sortBy { it.etudiant.nom }
            noteAdapter.notifyDataSetChanged()
        }

        findViewById<Button>(R.id.btnSortByNote).setOnClickListener {
            notes.sortByDescending { it.valeur }
            noteAdapter.notifyDataSetChanged()
        }

        // Charger les notes sauvegardées
        loadNotes()

        // Si aucune note n'est chargée, ajouter un exemple
        if (notes.isEmpty()) {
            val exampleNote = Note(
                Etudiant("Dupont", "Marie", "marie.dupont@email.com"),
                Matiere("Mathématiques", 3),
                16.5f
            )
            notes.add(exampleNote)

            val exampleNote2 = Note(
                Etudiant("Martin", "Thomas", "thomas.martin@email.com"),
                Matiere("Physique", 2),
                14f
            )
            notes.add(exampleNote2)

            noteAdapter.notifyDataSetChanged()
        }

        // Mettre à jour les statistiques
        updateStatistics()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                logout()
                true
            }
            R.id.action_about -> {
                showAboutDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showAboutDialog() {
        AlertDialog.Builder(this)
            .setTitle("À propos")
            .setMessage("Application de Gestion des Notes\nDéveloppée par Zakariae LAALIJI\nVersion 1.0")
            .setPositiveButton("OK", null)
            .show()
    }

    private fun logout() {
        // Supprimer le statut de connexion
        getSharedPreferences("NotesApp", MODE_PRIVATE)
            .edit()
            .putBoolean("isLoggedIn", false)
            .apply()

        // Rediriger vers le login
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun showDeleteConfirmation(position: Int) {
        val note = notes[position]
        AlertDialog.Builder(this)
            .setTitle("Supprimer la note")
            .setMessage("Êtes-vous sûr de vouloir supprimer la note de ${note.etudiant.prenom} ${note.etudiant.nom} en ${note.matiere.nom}?")
            .setPositiveButton("Supprimer") { _, _ ->
                notes.removeAt(position)
                noteAdapter.notifyItemRemoved(position)
                updateStatistics()
                saveNotes()
                Toast.makeText(this, "Note supprimée", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Annuler", null)
            .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_NOTE_REQUEST_CODE && resultCode == RESULT_OK) {
            val newNote = data?.getParcelableExtra<Note>("newNote")
            newNote?.let {
                notes.add(it)  // Ajouter la nouvelle note à la liste
                noteAdapter.updateList(notes)  // Mettre à jour la liste dans l'adaptateur
                updateStatistics()  // Mettre à jour les statistiques
                saveNotes()  // Sauvegarder les données
                Toast.makeText(this, "Note ajoutée avec succès", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun filterNotes(query: String) {
        if (query.isEmpty()) {
            noteAdapter.updateList(notes)
            return
        }

        val filteredList = notes.filter {
            it.etudiant.nom.contains(query, ignoreCase = true) ||
                    it.etudiant.prenom.contains(query, ignoreCase = true) ||
                    it.matiere.nom.contains(query, ignoreCase = true)
        }
        noteAdapter.updateList(filteredList)
    }

    private fun updateStatistics() {
        if (notes.isEmpty()) {
            statsTextView.text = "Aucune note enregistrée"
            return
        }

        val moyenne = notes.sumOf { it.valeur.toDouble() * it.matiere.coefficient } /
                notes.sumOf { it.matiere.coefficient.toDouble() }
        val max = notes.maxOf { it.valeur }
        val min = notes.minOf { it.valeur }
        val nbEtudiants = notes.distinctBy { it.etudiant.email }.size

        statsTextView.text = "📊 Moyenne pondérée: ${String.format("%.2f", moyenne)}/20 | " +
                "🔺 Max: $max | " +
                "🔻 Min: $min | " +
                "👥 Étudiants: $nbEtudiants | " +
                "📝 Notes: ${notes.size}"
    }

    private fun saveNotes() {
        val sharedPrefs = getSharedPreferences("NotesApp", MODE_PRIVATE)
        val json = android.text.TextUtils.join(";;", notes.map {
            "${it.etudiant.nom}::${it.etudiant.prenom}::${it.etudiant.email}::${it.matiere.nom}::${it.matiere.coefficient}::${it.valeur}"
        })
        sharedPrefs.edit().putString("notes", json).apply()
    }

    private fun loadNotes() {
        val sharedPrefs = getSharedPreferences("NotesApp", MODE_PRIVATE)
        val json = sharedPrefs.getString("notes", "")
        if (!json.isNullOrEmpty()) {
            notes.clear()
            val noteStrings = json.split(";;")
            for (noteStr in noteStrings) {
                val parts = noteStr.split("::")
                if (parts.size == 6) {
                    val etudiant = Etudiant(parts[0], parts[1], parts[2])
                    val matiere = Matiere(parts[3], parts[4].toInt())
                    val valeur = parts[5].toFloat()
                    notes.add(Note(etudiant, matiere, valeur))
                }
            }
            noteAdapter.notifyDataSetChanged()
        }
    }

    override fun onPause() {
        super.onPause()
        saveNotes()
    }
}
