package com.example.taller2.ui

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.MediaController
import android.widget.VideoView
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.example.taller2.media.MediaMode
import com.example.taller2.media.createTempMediaUri
import kotlinx.coroutines.launch

private enum class CameraAction {
    TAKE_PHOTO,
    RECORD_VIDEO
}

private data class PreviewMedia(
    val uri: Uri,
    val mode: MediaMode
)

@Composable
fun MediaScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var selectedMode by rememberSaveable { mutableStateOf(MediaMode.PHOTO) }
    var currentPreview by remember { mutableStateOf<PreviewMedia?>(null) }
    var pendingAction by remember { mutableStateOf<CameraAction?>(null) }
    var pendingOutputUri by remember { mutableStateOf<Uri?>(null) }

    val takePhotoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && pendingOutputUri != null) {
            currentPreview = PreviewMedia(pendingOutputUri!!, MediaMode.PHOTO)
        } else {
            scope.launch {
                snackbarHostState.showSnackbar("No se pudo capturar la foto.")
            }
        }
    }

    val recordVideoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CaptureVideo()
    ) { success ->
        if (success && pendingOutputUri != null) {
            currentPreview = PreviewMedia(pendingOutputUri!!, MediaMode.VIDEO)
        } else {
            scope.launch {
                snackbarHostState.showSnackbar("No se pudo grabar el video.")
            }
        }
    }

    val pickPhotoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            currentPreview = PreviewMedia(uri, MediaMode.PHOTO)
        }
    }

    val pickVideoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            currentPreview = PreviewMedia(uri, MediaMode.VIDEO)
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            when (pendingAction) {
                CameraAction.TAKE_PHOTO -> pendingOutputUri?.let(takePhotoLauncher::launch)
                CameraAction.RECORD_VIDEO -> pendingOutputUri?.let(recordVideoLauncher::launch)
                null -> Unit
            }
        } else {
            scope.launch {
                snackbarHostState.showSnackbar("Se requiere el permiso de cámara para continuar.")
            }
        }
    }

    fun requestCameraAction(action: CameraAction) {
        pendingAction = action
        pendingOutputUri = createTempMediaUri(
            context = context,
            mode = if (action == CameraAction.TAKE_PHOTO) MediaMode.PHOTO else MediaMode.VIDEO
        )

        val permissionGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        if (permissionGranted) {
            val outputUri = pendingOutputUri ?: return
            when (action) {
                CameraAction.TAKE_PHOTO -> takePhotoLauncher.launch(outputUri)
                CameraAction.RECORD_VIDEO -> recordVideoLauncher.launch(outputUri)
            }
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SnackbarHost(hostState = snackbarHostState)

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Selecciona el tipo de contenido",
                    style = MaterialTheme.typography.titleMedium
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Foto")
                    Switch(
                        checked = selectedMode == MediaMode.VIDEO,
                        onCheckedChange = { isVideo ->
                            selectedMode = if (isVideo) MediaMode.VIDEO else MediaMode.PHOTO
                            currentPreview = null
                        }
                    )
                    Text(text = "Video")
                }
                Text(
                    text = if (selectedMode == MediaMode.PHOTO) {
                        "Modo foto activado: puedes tomar o seleccionar una imagen."
                    } else {
                        "Modo video activado: puedes grabar o seleccionar un video."
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        requestCameraAction(
                            if (selectedMode == MediaMode.PHOTO) CameraAction.TAKE_PHOTO else CameraAction.RECORD_VIDEO
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (selectedMode == MediaMode.PHOTO) {
                            "Tomar Foto"
                        } else {
                            "Grabar Video"
                        }
                    )
                }

                Button(
                    onClick = {
                        if (selectedMode == MediaMode.PHOTO) {
                            pickPhotoLauncher.launch("image/*")
                        } else {
                            pickVideoLauncher.launch("video/*")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (selectedMode == MediaMode.PHOTO) {
                            "Seleccionar Foto"
                        } else {
                            "Seleccionar Video"
                        }
                    )
                }
            }
        }

        Text(
            text = "Vista previa",
            style = MaterialTheme.typography.titleMedium
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(340.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .size(width = 280.dp, height = 280.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant,
                        shape = RoundedCornerShape(24.dp)
                    ),
                shape = RoundedCornerShape(24.dp)
            ) {
                when (val preview = currentPreview) {
                    null -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Aquí se mostrará la foto o el video seleccionado.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(24.dp)
                            )
                        }
                    }

                    else -> {
                        if (preview.mode == MediaMode.PHOTO) {
                            AsyncImage(
                                model = preview.uri,
                                contentDescription = "Vista previa de la foto",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            AndroidView(
                                modifier = Modifier.fillMaxSize(),
                                factory = { androidContext ->
                                    VideoView(androidContext).apply {
                                        val mediaController = MediaController(androidContext)
                                        mediaController.setAnchorView(this)
                                        setMediaController(mediaController)
                                    }
                                },
                                update = { videoView ->
                                    videoView.setVideoURI(preview.uri)
                                    videoView.setOnPreparedListener { mediaPlayer ->
                                        mediaPlayer.isLooping = true
                                        videoView.start()
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

