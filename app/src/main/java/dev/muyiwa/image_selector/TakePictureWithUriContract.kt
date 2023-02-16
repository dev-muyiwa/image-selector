package dev.muyiwa.image_selector

import android.app.Activity
import android.content.*
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.contract.*
import androidx.annotation.CallSuper

class TakePictureWithUriContract: ActivityResultContract<Uri, Pair<Boolean, Uri>>() {
	private lateinit var imageUri: Uri

	@CallSuper
	override fun createIntent(context: Context, input: Uri): Intent {
		imageUri = input
		return Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, input)
	}

	override fun getSynchronousResult(
		context: Context,
		input: Uri
	): SynchronousResult<Pair<Boolean, Uri>>? = null

	@Suppress("AutoBoxing")
	override fun parseResult(resultCode: Int, intent: Intent?): Pair<Boolean, Uri> {
		return (resultCode == Activity.RESULT_OK) to imageUri
	}
}