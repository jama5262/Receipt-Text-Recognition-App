package com.jama.receipttextrecognitionapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.CameraX
import androidx.camera.core.Preview
import androidx.camera.core.PreviewConfig
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LifecycleOwner
import kotlinx.android.synthetic.main.fragment_camera.view.*

class CameraActivity : Fragment() {

    private lateinit var rootView: View
    private val CAMERA_PERMISSION_REQUEST_CODE = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_camera, container, false)

        checkPermissions()

        return rootView
    }

    private fun startCamera() {

        val previewConfig = PreviewConfig.Builder().run {
            setLensFacing(CameraX.LensFacing.BACK)
            build()
        }
        val preview = Preview(previewConfig)

        preview.setOnPreviewOutputUpdateListener {

            rootView.textureView.surfaceTexture = it.surfaceTexture
        }

        CameraX.bindToLifecycle(this as LifecycleOwner, preview)
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
                    // go back
                }
                return
            }
            else -> {
                // Ignore all other requests.
            }
        }
    }
}
