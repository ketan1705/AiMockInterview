package com.ken.aimockinterview.components

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.parser.PdfTextExtractor
import com.ken.aimockinterview.utils.Constants
import com.ken.aimockinterview.viewmodels.GeminiViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream

@Composable
fun TextExtractor(modifier: Modifier = Modifier) {
    val geminiViewModel: GeminiViewModel = hiltViewModel()

    val extractedText = remember {
        mutableStateOf("")
    }
    val ctx = LocalContext.current

    var scope = rememberCoroutineScope()
    var pdfUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val pdfLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        pdfUri = uri
        uri?.let { uri ->
            scope.launch {
                val inputStream = ctx.contentResolver.openInputStream(uri)
                extractData(extractedText, inputStream)
                extractedText.let {
                    geminiViewModel.extractUserResume(it.toString())
                }
                Log.d(Constants.TAG, "Extract Text From PDF: ${extractedText.value}")
            }
        }
        /*        uri?.let {
                    val filePath = uri.path
                    val fileName = filePath?.substringAfterLast("/")
                    println("Selected PDF: $fileName")
                }*/
    }

    Column {
        CustomizedText("Please Uplaod Your Resume", Color.White)

        OutlinedButton(
            onClick = {
                // Launch intent to pick PDF file
//                val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
//                    type = "application/pdf"
//                }
//                ctx.startActivity(intent)
                pdfLauncher.launch("application/pdf")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Color.White)
        ) {
            Text(
                "Upload PDF Resume",
                color = Color.White,
                modifier = Modifier.padding(8.dp)
            )
        }/*
        Button(
            onClick = {
                // Launch PDF picker
                pdfLauncher.launch("application/pdf")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text("Pick PDF")
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),

            onClick = {
                pdfUri?.let { uri ->
                    scope.launch {
                        val inputStream = ctx.contentResolver.openInputStream(uri)
                        extractData(extractedText, inputStream)
                        Log.d(Constants.TAG, "Extract Text From PDF: ${extractedText.value}")
                    }
                }
            }) {
            Text(modifier = Modifier.padding(6.dp), text = "Extract Text from PDF")
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),

            onClick = {
                extractedText.value = ""

            }) {

            Text(modifier = Modifier.padding(6.dp), text = "Clear Text")
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),

            onClick = {
                scope.launch {
                    geminiViewModel.extractUserResume(extractedText.value)
                }

            }) {

            Text(modifier = Modifier.padding(6.dp), text = "GET DATA")
        }
        Spacer(modifier = Modifier.height(10.dp))

        Text(text = extractedText.value, color = Color.Black, fontSize = 12.sp)

        TextField(
            value = extractedText.value,
            onValueChange = {
                extractedText.value = it
            },
        )
*/
    }
    /* LaunchedEffect(extractedText.value) {
         if (extractedText.value.isNotEmpty()) {
             geminiViewModel.extractUserResume(extractedText.value)
         }
     }*/

}

suspend fun extractData(
    extractedString: MutableState<String>,
    inputStream: InputStream?,
) {
    withContext(Dispatchers.IO) {
        try {
            var extractedText = ""

//            val inputStream: InputStream = context.resources.openRawResource(R.raw.android)
//            val pdfReader = PdfReader(inputStream)

            /*val pdfReader: PdfReader = PdfReader("res/raw/android.pdf")

            val n = pdfReader.numberOfPages

            for (i in 0 until n) {

                extractedText =
                    """
                 $extractedText${
                        PdfTextExtractor
                            .getTextFromPage(pdfReader, i + 1).trim { it <= ' ' }
                    }
                 
                 """.trimIndent()
            }

            extractedString.value = extractedText

            pdfReader.close()*/
            inputStream?.use { stream ->
                var extractedText = ""

                val pdfReader = PdfReader(stream)
                val n = pdfReader.numberOfPages

                for (i in 0 until n) {
                    extractedText += PdfTextExtractor
                        .getTextFromPage(pdfReader, i + 1)
                        .trim { it <= ' ' }
                }

                extractedString.value = extractedText
                pdfReader.close()
            } ?: run {
                extractedString.value = "No PDF selected"
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}