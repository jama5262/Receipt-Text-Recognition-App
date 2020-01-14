package com.jama.receipttextrecognitionapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_start_page.view.*

class StartPageFragement : Fragment() {

    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_start_page, container, false)

        rootView.buttonStart.setOnClickListener {
            rootView.findNavController().navigate(R.id.action_startPageActivity_to_cameraActivity)
        }

        return rootView
    }
}
