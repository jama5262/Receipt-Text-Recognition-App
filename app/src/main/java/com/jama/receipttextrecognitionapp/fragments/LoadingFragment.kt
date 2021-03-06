package com.jama.receipttextrecognitionapp.fragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.jama.receipttextrecognitionapp.R
import java.io.File

class LoadingFragment : Fragment() {

    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView =  inflater.inflate(R.layout.fragment_loading, container, false)

        detectText()

        return rootView
    }

    private fun detectText() {
        val uri = Uri.fromFile(File(arguments?.getString("imagePath")))
        val firebaseVisionImage = FirebaseVisionImage.fromFilePath(rootView.context, uri)

        val detector = FirebaseVision.getInstance().onDeviceTextRecognizer
        detector.processImage(firebaseVisionImage)
            .addOnSuccessListener { firebaseVisionText ->
                val regex = "[-+]?[0-9]*\\.[0-9]+".toRegex()
                val match= regex.findAll(firebaseVisionText.text)
                val arrayMatch = arrayListOf<Float>()
                for (i in match) {
                    arrayMatch.add(i.value.toFloat())
                }
                val total = arrayMatch.max() ?: 0f
                val bundle = bundleOf("total" to total)
                rootView.findNavController().navigate(R.id.action_loadingFragment_to_resultsFragment, bundle)
            }
            .addOnFailureListener {
                Log.e("jjj", "error -> ${it.message}")
                Toast.makeText(rootView.context, "OOps, there was an error detecting the total cost of receipt, please try again", Toast.LENGTH_LONG).show()
                activity?.onBackPressed()
            }
    }
}
