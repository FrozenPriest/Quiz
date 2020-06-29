package ru.frozenpriest.quiz

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Question(val question : String, val answers : List<String>, val rightAnswerIndex : Int) : Parcelable {

}