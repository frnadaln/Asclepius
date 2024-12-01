package com.dicoding.asclepius.view

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.database.HistoryEntity
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.viewmodel.CancerViewModel
import com.dicoding.asclepius.viewmodel.CancerViewModelFactory

@Suppress("DEPRECATION")
class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var imageClassifierHelper: ImageClassifierHelper

    private val viewModel: CancerViewModel by viewModels { CancerViewModelFactory(application) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageClassifierHelper = ImageClassifierHelper(this)

        val imageUri = intent.getParcelableExtra<Uri>("IMAGE_URI")

        imageUri?.let {
            binding.resultImage.setImageURI(it)
            imageClassifierHelper.classifyStaticImage(it) { label, confidence ->
                runOnUiThread {
                    val confidenceScore = "%.2f".format(confidence * 100)
                    val resultText = "Result: $label\nConfidence Score: $confidenceScore%"
                    binding.resultText.text = resultText

                    // Enable the save button once the result is ready
                    binding.saveButton.visibility = View.VISIBLE

                    // Save the prediction when the save button is clicked
                    binding.saveButton.setOnClickListener {
                        savePredictionToHistory(imageUri, label, confidenceScore)
                    }
                }
            }
        }
    }

    private fun savePredictionToHistory(imageUri: Uri, label: String, confidenceScore: String) {
        val historyEntity = HistoryEntity(
            image = imageUri.toString(),
            result = label,
            confidenceScore = confidenceScore,
            isSuccess = true
        )
        viewModel.savePrediction(historyEntity)
        Toast.makeText(this, "Prediction saved to history", Toast.LENGTH_SHORT).show()
    }
}
