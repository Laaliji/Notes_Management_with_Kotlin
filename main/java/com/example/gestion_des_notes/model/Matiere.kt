package com.example.gestion_des_notes.model

import android.os.Parcel
import android.os.Parcelable

data class Matiere(
    val nom: String,
    val coefficient: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nom)
        parcel.writeInt(coefficient)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Matiere> {
        override fun createFromParcel(parcel: Parcel): Matiere {
            return Matiere(parcel)
        }

        override fun newArray(size: Int): Array<Matiere?> {
            return arrayOfNulls(size)
        }
    }
}