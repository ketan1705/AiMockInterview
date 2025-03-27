package com.ken.aimockinterview.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Composable
fun PDFPicker(modifier: Modifier = Modifier) {
    val ctx = LocalContext.current
    var pdfUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val pdfLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        pdfUri = uri
        /*        uri?.let {
                    val filePath = uri.path
                    val fileName = filePath?.substringAfterLast("/")
                    println("Selected PDF: $fileName")
                }*/
    }

    TextExtractor()
    

}