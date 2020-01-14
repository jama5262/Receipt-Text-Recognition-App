package com.jama.receipttextrecognitionapp

import android.Manifest
import android.content.pm.PackageManager
import android.opengl.Visibility
import android.os.Bundle
import android.os.Environment
import android.util.Log
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
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
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

        rootView.buttonCapture.setOnClickListener {
//            capture()
            val bundle = bundleOf("imagePath" to "JAMA MOHAMED")
            rootView.findNavController().navigate(R.id.action_cameraActivity_to_loadingFragment, bundle)
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
        val previewConfig = PreviewConfig.Builder().build()
        val preview = Preview(previewConfig)

        preview.setOnPreviewOutputUpdateListener {

            rootView.textureView.surfaceTexture = it.surfaceTexture
        }

        val imageCaptureConfig = ImageCaptureConfig.Builder()
            .setTargetRotation(activity!!.windowManager.defaultDisplay.rotation)
            .build()

        imageCapture = ImageCapture(imageCaptureConfig)

        CameraX.bindToLifecycle(this as LifecycleOwner, imageCapture, preview)
    }

    private fun capture() {
        rootView.progressBar.visibility = View.VISIBLE
        rootView.buttonCapture.isEnabled = false
        val file = File(activity!!.externalMediaDirs.first(), "${System.currentTimeMillis()}.jpg")
        imageCapture.takePicture(file,
            object : ImageCapture.OnImageSavedListener {
                override fun onError(error: ImageCapture.ImageCaptureError,
                                     message: String, exc: Throwable?) {
                    Log.e("jjj", "error -> $message")
                    rootView.progressBar.visibility = View.GONE
                }
                override fun onImageSaved(file: File) {
                    Log.e("jjj", "error -> ${file.absolutePath}")
                    rootView.progressBar.visibility = View.GONE
                    rootView.findNavController().navigate(R.id.action_cameraActivity_to_loadingFragment)
                }
            })
    }

    private fun rotationDegreesToFirebaseRotation(rotationDegrees: Int) = when (rotationDegrees) {
        0 -> FirebaseVisionImageMetadata.ROTATION_0
        90 -> FirebaseVisionImageMetadata.ROTATION_90
        180 -> FirebaseVisionImageMetadata.ROTATION_180
        270 -> FirebaseVisionImageMetadata.ROTATION_270
        else -> throw IllegalArgumentException("Rotation $rotationDegrees not supported")
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
