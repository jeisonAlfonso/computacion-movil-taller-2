package com.example.taller2.media

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class MediaMode {
    PHOTO,
    VIDEO
}

fun createTempMediaUri(context: Context, mode: MediaMode): Uri {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val directory = File(context.externalCacheDir ?: context.cacheDir, "captured_media").apply {
        mkdirs()
    }
    val extension = if (mode == MediaMode.PHOTO) ".jpg" else ".mp4"
    val prefix = if (mode == MediaMode.PHOTO) "IMG_" else "VID_"
    val file = File.createTempFile(prefix + timeStamp + "_", extension, directory)
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file
    )
}

