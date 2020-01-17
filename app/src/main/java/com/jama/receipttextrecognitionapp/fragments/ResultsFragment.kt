package com.jama.receipttextrecognitionapp.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.jama.receipttextrecognitionapp.R
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

        rootView.textViewTotal.text = arguments?.getFloat("total").toString()

        rootView.customButtonYes.setOnClickListener {
            disableButtons()
            loadGif("success")
            showAnimation(0)
            rootView.textViewTotal.setTextColor(ContextCompat.getColor(rootView.context, R.color.colorPrimary))
        }

        rootView.customButtonNo.setOnClickListener {
            disableButtons()
            loadGif("failure")
            showAnimation(1)
            rootView.textViewTotal.setTextColor(ContextCompat.getColor(rootView.context, R.color.colorError))
        }

        rootView.customButtonTryAgain.setOnClickListener {
            activity?.onBackPressed()
        }

        return rootView
    }

    private fun showAnimation(value: Int) {
        YoYo.with(
            when(value) {
                0 -> Techniques.Tada
                else -> Techniques.Shake
            }
        )
            .duration(1000)
            .playOn(rootView.textViewTotal)
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
            try {
                val response = GiphyAPI().getGif(tag).data.imageUrl
                Glide
                    .with(rootView.context)
                    .asGif()
                    .load(response)
                    .into(rootView.imageView)
            } catch (e: Exception) {
                Glide
                    .with(rootView.context)
                    .asGif()
                    .load(if (tag == "success") R.drawable.success else R.drawable.failure)
                    .into(rootView.imageView)
            }
            rootView.progressBar.visibility = View.GONE
        }
    }



}
