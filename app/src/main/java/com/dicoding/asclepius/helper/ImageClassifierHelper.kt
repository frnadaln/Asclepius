package com.dicoding.asclepius.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.vision.classifier.ImageClassifier
import java.io.IOException

class ImageClassifierHelper(private val context: Context) {

    private var imageClassifier: ImageClassifier? = null

    init {
        setupImageClassifier()
    }

    private fun setupImageClassifier() {
        try {
            imageClassifier = ImageClassifier.createFromFile(context, "cancer_classification.tflite")
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun classifyStaticImage(imageUri: Uri, callback: (String, Float) -> Unit) {
        try {
            val bitmap = context.contentResolver.openInputStream(imageUri).use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            }?.copy(Bitmap.Config.ARGB_8888, true)

            bitmap?.let {
                val tensorImage = TensorImage.fromBitmap(it)
                val results = imageClassifier?.classify(tensorImage)

                results?.firstOrNull()?.categories?.firstOrNull()?.let { category ->
                    callback(category.label, category.score)
                } ?: callback("No Result", 0.0f)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            callback("Error: ${e.message}", 0.0f)
        }
    }
}
