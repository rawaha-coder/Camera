package com.hybcode.camera.ui.camera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.hybcode.camera.MainActivity
import com.hybcode.camera.R
import com.hybcode.camera.databinding.FragmentCameraBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraFragment : Fragment() {

    private var _binding: FragmentCameraBinding? = null
    private lateinit var cameraExecutor: ExecutorService

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCameraBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cameraExecutor = Executors.newSingleThreadExecutor()
        openCamera()
    }

    private fun openCamera(){
        if (MainActivity.CameraPermissionHelper.hasCameraPermission(requireActivity()) &&
            MainActivity.CameraPermissionHelper.hasStoragePermission(requireActivity())) {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(requireActivity())
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(binding.cameraFeed.surfaceProvider)
                    }
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                // TODO: Initialise the ImageCapture instance here
                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(this, cameraSelector, preview)
                } catch (e: IllegalStateException) {
                    Toast.makeText(requireActivity(), resources.getString(R.string.error_connecting_camera),
                        Toast.LENGTH_LONG).show()
                }
            }, ContextCompat.getMainExecutor(requireActivity()))
        } else MainActivity.CameraPermissionHelper.requestPermissions(requireActivity())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}