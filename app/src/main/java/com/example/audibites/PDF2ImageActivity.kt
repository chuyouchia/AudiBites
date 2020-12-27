package com.example.audibites;

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask


class PDF2ImageActivity : AppCompatActivity() {
    private var mStorageRef: StorageReference? = null
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
        mStorageRef = FirebaseStorage.getInstance().reference;

    }

    private var selectedPDFUri: Uri? = null
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.d("PDF Selection Activity", "Document was selected")
            //proceed to select the pdf file
            selectedPDFUri = data.data

            //send the pdf file to the server to be processed
            sendToAppServer(selectedPDFUri!!)

            //get back the voice file
            getAudioFromBackend(selectedPDFUri!!)
        }
    }

    private fun getAudioFromBackend(uri: Uri) {
        //get the python backend to process the file via api call

        //get the processed file via simple download //todo change to streamed download
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun sendToAppServer(uri: Uri) {
        //set the path of the file dynamically using its original file structure
        val riversRef: StorageReference? = mStorageRef?.child(uri.path!!);

        riversRef!!.putFile(uri)
            .addOnSuccessListener(this,  OnSuccessListener<UploadTask.TaskSnapshot>() {
                print("success!")
                //TODO get the voice file
            }).addOnFailureListener {
                    // Handle unsuccessful uploads
                    print("failed")
            }
    }



}
