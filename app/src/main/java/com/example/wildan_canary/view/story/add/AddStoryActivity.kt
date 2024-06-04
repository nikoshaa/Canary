package com.example.wildan_canary.view.story.add

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.wildan_canary.R
import com.example.wildan_canary.databinding.ActivityAddStoryBinding
import com.example.wildan_canary.helper.ViewModelFactory
import com.example.wildan_canary.helper.adapter.AdapterStory
import com.example.wildan_canary.data.network.response.Result
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class AddStoryActivity : AppCompatActivity() {

    private val imageViewModel: ImageViewModel by viewModels() // Inisialisasi tanpa ViewModelFactory
    private lateinit var binding: ActivityAddStoryBinding
    private var uploadFile: File? = null
    private var positionLat: RequestBody? = null
    private var positionLon: RequestBody? = null
    private var location: Location? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val addStoryViewModel: AddStoryViewModel by viewModels {
        ViewModelFactory(this)
    }
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                getLastLocation()
            } else {
                Toast.makeText(this, "Izin lokasi diperlukan untuk aplikasi ini", Toast.LENGTH_SHORT).show()
            }
        }
    companion object {
        fun start(context: Context) {
            val intent = Intent(context, AddStoryActivity::class.java)
            context.startActivity(intent)
        }
        private val LOCATION_PERMISSION_REQUEST_CODE = 1

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        imageViewModel.selectedImage.observe(this) { uri ->
            binding.imgPreview.setImageURI(uri)
        }

        binding.apply {
            btnOpenGallery.setOnClickListener {
                ImagePicker.with(this@AddStoryActivity)
                    .crop()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .start()
            }

            cbLocation.setOnCheckedChangeListener{ _, isChecked ->
                if (isChecked) {
                    checkLocationPermission()
                }else {
                    cbLocation.text = getString(R.string.share_location)
                }
            }

            buttonAdd.setOnClickListener {
                if (edAddDescription.text.toString().isEmpty()) {
                    Toast.makeText(
                        this@AddStoryActivity,
                        "Description is Empty",
                        Toast.LENGTH_LONG
                    ).show()
                    return@setOnClickListener
                }
                uploadStory()
            }
        }

        addStoryViewModel.responseResult.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "Story uploaded", Toast.LENGTH_LONG).show()
                    setResult(RESULT_OK)
                    finish()
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE

                }
            }
        }
    }
    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    val address = AdapterStory.parseAddressLocation(this, it.latitude, it.longitude)
                    binding.cbLocation.text = address
                }
            }
    }
    private fun checkLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                getLastLocation()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                Toast.makeText(this, "Aplikasi memerlukan izin lokasi untuk menampilkan lokasi Anda.", Toast.LENGTH_LONG).show()
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }
    private fun uploadStory() {
        uploadFile?.let {
            val imageFileStory = reduceFileImage(uploadFile as File)
            val description = binding.edAddDescription.text.toString()
            val requestBodyDescription = description.toRequestBody("text/plain".toMediaType())
            val latRequestBody = positionLat
            val lonRequestBody = positionLon
            val requestImageFile = imageFileStory.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData("photo", imageFileStory.name, requestImageFile)
            addStoryViewModel.addStory(multipartBody, requestBodyDescription, latRequestBody, lonRequestBody)
        } ?: Toast.makeText(this, "Image is Empty", Toast.LENGTH_LONG).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == ImagePicker.REQUEST_CODE) {
            val uri: Uri? = data?.data
            uri?.let {
                val imageFile = File(this.cacheDir, "image_${System.currentTimeMillis()}.jpg")
                val outputStream = FileOutputStream(imageFile)
                val inputStream = this.contentResolver.openInputStream(uri)
                inputStream?.use { input ->
                    outputStream.use { output ->
                        input.copyTo(output)
                    }
                }
                imageViewModel.setSelectedImage(Uri.fromFile(imageFile))
                uploadFile = imageFile

            }
        }
    }

    fun reduceFileImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        var compressQuality = 100
        var streamLength: Int
        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > 1000000)
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
        return file
    }
}
