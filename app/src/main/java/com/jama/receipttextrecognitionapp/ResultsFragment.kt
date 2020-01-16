package com.jama.receipttextrecognitionapp

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jama.receipttextrecognitionapp.services.GiphyAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ResultsFragment : Fragment() {

    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_results, container, false)
        return rootView
    }


//    private fun loadGif() {
//        CoroutineScope(Dispatchers.IO).launch {
//            successResponse = GiphyAPI().getGif("success").data.imageUrl
//            delay(2000)
//            failureResponse = JsonPlaceHolderAPI().getGif("failure").data.imageUrl
//            Log.e("jjj", "$successResponse $failureResponse")
//        }
//    }

}
