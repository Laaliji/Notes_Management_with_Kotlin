package com.example.gestion_des_notes.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.gestion_des_notes.R
import com.example.gestion_des_notes.model.Note

class NoteAdapter(
    private val notes: MutableList<Note>,
    private val onLongClick: (Int) -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private var displayedNotes: List<Note> = notes.toList()

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.findViewById(R.id.cardView)
        val nomPrenomTextView: TextView = itemView.findViewById(R.id.nomPrenomTextView)
        val emailTextView: TextView = itemView.findViewById(R.id.emailTextView)
        val matiereTextView: TextView = itemView.findViewById(R.id.matiereTextView)
        val noteTextView: TextView = itemView.findViewById(R.id.noteTextView)

        init {
            itemView.setOnLongClickListener {
                val position = notes.indexOf(displayedNotes[adapterPosition])
                if (position != -1) {
                    onLongClick(position)
                }
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = displayedNotes[position]
        holder.nomPrenomTextView.text = "${note.etudiant.nom} ${note.etudiant.prenom}"
        holder.emailTextView.text = note.etudiant.email
        holder.matiereTextView.text = "${note.matiere.nom} (Coeff: ${note.matiere.coefficient})"
        holder.noteTextView.text = String.format("%.1f", note.valeur)

        // Couleur en fonction de la note
        val colorBg = when {
            note.valeur >= 16 -> Color.parseColor("#DCEDC8") // Vert clair
            note.valeur >= 10 -> Color.parseColor("#E3F2FD") // Bleu clair
            else -> Color.parseColor("#FFEBEE") // Rouge clair
        }

        val colorText = when {
            note.valeur >= 16 -> Color.parseColor("#33691E") // Vert foncé
            note.valeur >= 10 -> Color.parseColor("#1565C0") // Bleu foncé
            else -> Color.parseColor("#B71C1C") // Rouge foncé
        }

        holder.cardView.setCardBackgroundColor(colorBg)
        holder.noteTextView.setTextColor(colorText)

        // Animation subtile au chargement
        holder.itemView.alpha = 0f
        holder.itemView.animate().alpha(1f).setDuration(300).start()
    }

    override fun getItemCount(): Int = displayedNotes.size

    fun updateList(newNotes: List<Note>) {
        displayedNotes = newNotes
        notifyDataSetChanged()
    }
}