package com.example.audibites;

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.*
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

class PDF2ImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pdf2image)

        val searchForPdfButton = findViewById<Button>(R.id.pick_pdf_file_button_pdf2image)
        val backToMainActivityButton = findViewById<Button>(R.id.back_to_main_button)

        searchForPdfButton.setOnClickListener {
            Toast.makeText(this, "You are searching phone.",
                    Toast.LENGTH_SHORT).show()

            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*";
            startActivityForResult(intent, 0)
        }

        backToMainActivityButton.setOnClickListener{
            Toast.makeText(this, "You are heading back to main page.",
                    Toast.LENGTH_SHORT).show()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

    private var selectedPDFUri: Uri? = null
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.d("PDF Selection Activity", "Document was selected")
            //proceed to select the pdf file
            selectedPDFUri = data.data

            //process the pdf into a series of bitmaps
            load(selectedPDFUri!!)

        }
    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun load(uri: Uri){
        //convert uri to file object
        /*val `in`: InputStream? = contentResolver.openInputStream(uri)
        val documentFileOutputStream: OutputStream = FileOutputStream(File(uri.path))
        val buf = ByteArray(1024)
        var len: Int
        while (`in`!!.read(buf).also { len = it } > 0) {
            documentFileOutputStream.write(buf, 0, len)
        }
        documentFileOutputStream.close()
        `in`.close()
         */

        // Create the page renderer for the PDF document.
        val fileDescriptor = contentResolver.openFileDescriptor(uri, "r")
        val pdfRenderer = PdfRenderer(fileDescriptor!!)

        for (i in 0 until pdfRenderer.pageCount) {

            val page = pdfRenderer.openPage(i)
            Log.d("PDF renderer", "current page saving is: ${i.toString()}")
            // Render the page to the bitmap.
            val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
            saveTempBitmap(bitmap)

            // close the page
            page.close()
        }

        // Close the `PdfRenderer` when you are done with it.
        pdfRenderer.close()
    }

    private fun saveTempBitmap(bitmap: Bitmap) {
        if (isExternalStorageWritable()) {
            saveImage(bitmap)
        } else {
            //prompt the user or do something by displaying error message
            Toast.makeText(this, "Unable to save", Toast.LENGTH_SHORT)
        }
    }

    private fun saveImage(finalBitmap: Bitmap) {
        val root: String = Environment.getExternalStorageState()
        val myDir = File("$root/audibites/saved_images")
        myDir.mkdirs()
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val fname = "Audibites_$timeStamp.jpg"
        val file = File(myDir, fname)


        if (!file.exists()) {
            file.createNewFile();
        } else {
            file.delete()
        }
        try {
            val out = FileOutputStream(file)
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /* Checks if external storage is available for read and write */
    private fun isExternalStorageWritable(): Boolean {
        val state: String = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == state
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

}
