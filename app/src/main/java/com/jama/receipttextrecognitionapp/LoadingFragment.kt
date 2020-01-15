package com.jama.receipttextrecognitionapp

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import java.io.File
import java.io.IOException

class LoadingFragment : Fragment() {

    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_loading, container, false)



        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detectText()
    }

    private fun detectText() {
        val uri = Uri.fromFile(File("/storage/emulated/0/Android/media/com.jama.receipttextrecognitionapp/1578994465864.jpg"))
        val firebaseVisionImage = FirebaseVisionImage.fromFilePath(rootView.context, uri)

        val detector = FirebaseVision.getInstance().onDeviceTextRecognizer
        detector.processImage(firebaseVisionImage)
            .addOnSuccessListener {
                val regex = "[-+]?[0-9]*\\.[0-9]+".toRegex()
                val res= regex.findAll(it.text)
                Log.e("jjj", "success -> ${it.text}")
                for (i in res) {
                    Log.e("jjj", "success -> ${i.value}")
                }
            }
            .addOnFailureListener {
                Log.e("jjj", "error -> ${it.message}")
            }
    }
}
