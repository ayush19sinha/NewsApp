package my.android.newsapp.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

// This function formats the given date string to a specified format ("dd-MM-yyyy HH:mm:ss").
@RequiresApi(Build.VERSION_CODES.O)
fun dateFormater(date:String) : String{
    val inputFormatter = DateTimeFormatter.ISO_DATE_TIME
    val zonedDateTime = ZonedDateTime.parse(date, inputFormatter)
    val outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
    val formattedDate = zonedDateTime.format(outputFormatter)
    return formattedDate
}