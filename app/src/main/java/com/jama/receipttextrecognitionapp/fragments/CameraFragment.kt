package com.jama.receipttextrecognitionapp.fragments

import android.os.Bundle
import android.util.Log
import android.util.Rational
import android.util.Size
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.*
import androidx.core.os.bundleOf
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.findNavController
import com.jama.receipttextrecognitionapp.R
import kotlinx.android.synthetic.main.fragment_camera.view.*
import java.io.File

class CameraFragment : Fragment() {

    private lateinit var rootView: View
    private lateinit var imageCapture: ImageCapture

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_camera, container, false)

        rootView.textViewMessage.visibility = View.GONE

        rootView.buttonCapture.setOnClickListener {
            capture()
        }

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startCamera()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        CameraX.unbindAll()
    }

    private fun startCamera() {
        val previewConfig = PreviewConfig.Builder()
            .setTargetAspectRatio(Rational (rootView.textureView.width, rootView.textureView.height))
            .setTargetResolution(Size (rootView.textureView.width, rootView.textureView.height))
            .build()
        val preview = Preview(previewConfig)

        preview.setOnPreviewOutputUpdateListener {

            rootView.textureView.surfaceTexture = it.surfaceTexture
        }

        val imageCaptureConfig = ImageCaptureConfig.Builder()
            .setTargetRotation(activity!!.windowManager.defaultDisplay.rotation)
            .setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
            .build()

        imageCapture = ImageCapture(imageCaptureConfig)

        CameraX.bindToLifecycle(this as LifecycleOwner, imageCapture, preview)
    }

    private fun capture() {
        rootView.progressBar.visibility = View.VISIBLE
        rootView.textViewMessage.visibility = View.VISIBLE
        rootView.buttonCapture.isEnabled = false
        val file = File(activity!!.externalMediaDirs.first(), "${System.currentTimeMillis()}.jpg")
        imageCapture.takePicture(file,
            object : ImageCapture.OnImageSavedListener {
                override fun onError(error: ImageCapture.ImageCaptureError,
                                     message: String, exc: Throwable?) {
                    Toast.makeText(rootView.context, "OOps, there was an error taking the picture, please try again", Toast.LENGTH_LONG).show()
                    activity?.onBackPressed()
                }
                override fun onImageSaved(file: File) {
                    rootView.progressBar.visibility = View.GONE
                    val emoji = String(Character.toChars(0x1F44D))
                    rootView.textViewMessage.text = "OK got it $emoji"
                    Log.e("jjj", "success -> ${file.absolutePath}")
                    val bundle = bundleOf("imagePath" to file.absolutePath)
                    rootView.findNavController().navigate(R.id.action_cameraActivity_to_loadingFragment, bundle)
                }
            })
    }
}
