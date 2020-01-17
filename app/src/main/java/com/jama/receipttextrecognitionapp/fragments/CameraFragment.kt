package com.jama.receipttextrecognitionapp.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.util.Rational
import android.util.Size
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.*
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.findNavController
import com.jama.receipttextrecognitionapp.R
import kotlinx.android.synthetic.main.fragment_camera.view.*
import java.io.File

class CameraFragment : Fragment() {

    private lateinit var rootView: View
    private val CAMERA_PERMISSION_REQUEST_CODE = 1
    private lateinit var imageCapture: ImageCapture

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_camera, container, false)

        rootView.textViewMessage.visibility = View.GONE

        rootView.buttonCapture.setOnClickListener {
            capture()
        }

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermissions()
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
                    Log.e("jjj", "error -> $message")
                    rootView.progressBar.visibility = View.GONE
                    rootView.textViewMessage.visibility = View.GONE
                }
                override fun onImageSaved(file: File) {
                    rootView.progressBar.visibility = View.GONE
                    val t = String(Character.toChars(0x1F60A))
                    rootView.textViewMessage.text = "OK got it $t"
                    Log.e("jjj", "success -> ${file.absolutePath}")
                    val bundle = bundleOf("imagePath" to file.absolutePath)
                    rootView.findNavController().navigate(R.id.action_cameraActivity_to_loadingFragment, bundle)
                }
            })
    }

    private fun checkPermissions() {
        if (ActivityCompat.checkSelfPermission(rootView.context, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(rootView.context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), CAMERA_PERMISSION_REQUEST_CODE)
        } else {
            startCamera()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            CAMERA_PERMISSION_REQUEST_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    startCamera()
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    activity!!.onBackPressed()
                }
                return
            }
            else -> {
                // Ignore all other requests.
            }
        }
    }
}
