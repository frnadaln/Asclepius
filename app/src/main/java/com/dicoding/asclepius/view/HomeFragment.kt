package com.dicoding.asclepius.view

import android.animation.AnimatorInflater
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.FragmentHomeBinding
import com.dicoding.asclepius.viewmodel.CancerViewModel
import com.yalantis.ucrop.UCrop
import java.io.File

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var currentImageUri: Uri? = null
    private var originalImageUri: Uri? = null

    private val viewModel: CancerViewModel by viewModels()

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri? = result.data?.data
            selectedImg?.let {
                originalImageUri = it
                currentImageUri = it
                launchUcrop(it)
            }
        } else {
            showToast("No image selected.")
        }
    }

    private val launcherUCrop = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { data ->
        when (data.resultCode) {
            RESULT_OK -> {
                viewModel.setCurrentImage(data.data?.let { UCrop.getOutput(it) })
            }
            UCrop.RESULT_ERROR -> {
                UCrop.getError(data.data!!)?.let {
                    showToast(it.message.toString())
                }
            }
            else -> {
                viewModel.setCurrentImage(originalImageUri)
            }
        }
    }

    private fun launchUcrop(uri: Uri) {
        val uriPath = Uri.fromFile(File.createTempFile("cropped_", ".jpg", requireActivity().cacheDir))
        launcherUCrop.launch(
            UCrop.of(uri, uriPath)
                .getIntent(requireActivity())
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.curImgUri.observe(viewLifecycleOwner) {
            currentImageUri = it
            showImage()
        }

        binding.galleryButton.setOnClickListener {
            startGallery()
        }

        binding.analyzeButton.setOnClickListener {
            animateAnalyzeButton()
            moveToResult()
        }
    }

    private fun startGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.previewImageView.setImageURI(it)
            try {
                val fadeInAnimation = AnimationUtils.loadAnimation(context, R.anim.fade_in)
                binding.previewImageView.startAnimation(fadeInAnimation)
            } catch (e: Exception) {
                Log.e("HomeFragment", "Error during image fade-in: ${e.message}")
            }
        } ?: run {
            Log.e("HomeFragment", "No image URI available.")
        }
    }

    private fun animateAnalyzeButton() {
        try {
            val buttonPressAnimation = AnimatorInflater.loadAnimator(context, R.animator.button_press)
            buttonPressAnimation.setTarget(binding.analyzeButton)
            buttonPressAnimation.setTarget(binding.galleryButton)
            buttonPressAnimation.start()
        } catch (e: Exception) {
            Log.e("HomeFragment", "Error during button press animation: ${e.message}")
        }
    }

    private fun moveToResult() {
        currentImageUri?.let { imageUri ->
            binding.progressIndicator.visibility = View.VISIBLE

            Thread {
                Thread.sleep(2000)
                requireActivity().runOnUiThread {
                    binding.progressIndicator.visibility = View.GONE
                    val intent = Intent(requireContext(), ResultActivity::class.java).apply {
                        putExtra("IMAGE_URI", imageUri)
                    }
                    startActivity(intent)
                }
            }.start()
        } ?: showToast("No image to analyze, please input an image.")
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
