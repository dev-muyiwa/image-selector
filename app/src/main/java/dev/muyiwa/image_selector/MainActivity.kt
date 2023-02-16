package dev.muyiwa.image_selector

import android.net.*
import android.os.*
import android.widget.*
import androidx.activity.result.contract.*
import androidx.appcompat.app.*
import androidx.core.content.*
import androidx.lifecycle.*
import dev.muyiwa.image_selector.databinding.*
import java.io.*

class MainActivity : AppCompatActivity() {
	private lateinit var binding: ActivityMainBinding

	private val chosenImage by lazy { binding.chosenImage }
	private val takeImageContract =
		registerForActivityResult(TakePictureWithUriContract()) { (isSuccessful, uri) ->
			if (isSuccessful) {
				chosenImage.setImageURI(uri)
			} else {
				Toast.makeText(applicationContext, "Unable to take pictures", Toast.LENGTH_SHORT)
					.show()
			}
		}
	private val selectImageContract =
		registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
			uri?.let { chosenImage.setImageURI(uri) }
		}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)

		binding.takeImage.setOnClickListener { takeImage() }
		binding.selectImage.setOnClickListener { selectImage() }
	}

	private fun getTmpUri(): Uri {
		val tmpFile = File.createTempFile("tmp_image_file", ".png", cacheDir).apply {
			createNewFile()
			deleteOnExit()
		}
		return FileProvider.getUriForFile(
			applicationContext,
			"dev.muyiwa.image_selector.provider",
			tmpFile
		)
	}

	private fun selectImage() = selectImageContract.launch("image/*")

	private fun takeImage() {
		lifecycleScope.launchWhenStarted {
			getTmpUri().let { uri ->
				takeImageContract.launch(uri)
			}
		}
	}
}