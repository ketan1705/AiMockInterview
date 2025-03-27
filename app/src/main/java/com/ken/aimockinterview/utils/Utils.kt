package com.ken.aimockinterview.utils

import android.util.Log
import com.ken.aimockinterview.models.QuesAnsModel
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Utils {

    companion object {
        fun convertTimestampToDate(timestamp: Long): String {
            try {
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                return sdf.format(Date(timestamp))
            } catch (e: Exception) {
                Log.e(Constants.TAG, "Error converting timestamp to date: ${e.message}")
                return ""
            }
        }

        /*     fun convertListToJson(interviewList: List<QuesAnsModel>): String {
                 return Json.encodeToString(interviewList)
             }
     */
        fun convertJsonToList(jsonString: String): List<QuesAnsModel> {
            return Json.decodeFromString(jsonString)
        }
    }
}