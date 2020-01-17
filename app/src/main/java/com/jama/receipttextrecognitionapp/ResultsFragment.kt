package com.jama.receipttextrecognitionapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.jama.receipttextrecognitionapp.services.GiphyAPI
import kotlinx.android.synthetic.main.fragment_results.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ResultsFragment : Fragment() {

    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_results, container, false)

        rootView.customButtonTryAgain.visibility = View.GONE
        rootView.imageView.visibility = View.GONE
        rootView.progressBar.visibility = View.GONE

        rootView.customButtonYes.setOnClickListener {
            disableButtons()
            loadGif("success")
        }

        rootView.customButtonNo.setOnClickListener {
            disableButtons()
            loadGif("failure")
        }

        rootView.customButtonTryAgain.setOnClickListener {
            activity?.onBackPressed()
        }

        return rootView
    }

    private fun disableButtons() {
        rootView.customButtonYes.isEnabled = false
        rootView.customButtonNo.isEnabled = false
        rootView.customButtonTryAgain.visibility = View.VISIBLE
        rootView.imageView.visibility = View.VISIBLE
        rootView.progressBar.visibility = View.VISIBLE
    }


    private fun loadGif(tag: String) {
        CoroutineScope(Dispatchers.Main).launch {
            val response = GiphyAPI().getGif(tag).data.imageUrl
            Glide
                .with(rootView.context)
                .load(response)
                .into(rootView.imageView)
            rootView.progressBar.visibility = View.GONE
        }
    }



}
