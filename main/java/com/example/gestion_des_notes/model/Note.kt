package com.example.gestion_des_notes.model

import android.os.Parcel
import android.os.Parcelable

data class Note(
    val etudiant: Etudiant,
    val matiere: Matiere,
    val valeur: Float
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Etudiant::class.java.classLoader) ?: Etudiant("", "", ""),
        parcel.readParcelable(Matiere::class.java.classLoader) ?: Matiere("", 1),
        parcel.readFloat()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(etudiant, flags)
        parcel.writeParcelable(matiere, flags)
        parcel.writeFloat(valeur)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Note> {
        override fun createFromParcel(parcel: Parcel): Note {
            return Note(parcel)
        }

        override fun newArray(size: Int): Array<Note?> {
            return arrayOfNulls(size)
        }
    }
}