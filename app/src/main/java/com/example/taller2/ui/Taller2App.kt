package com.example.taller2.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

private enum class TallerScreen {
    Home,
    Media,
    Map
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Taller2App() {
    var currentScreen by rememberSaveable { mutableStateOf(TallerScreen.Home) }

    Scaffold(
        topBar = {
            if (currentScreen != TallerScreen.Home) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = when (currentScreen) {
                                TallerScreen.Media -> "Galería, foto y video"
                                TallerScreen.Map -> "Mapa y localización"
                                TallerScreen.Home -> "Taller 2"
                            }
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { currentScreen = TallerScreen.Home }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver"
                            )
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        when (currentScreen) {
            TallerScreen.Home -> HomeScreen(
                innerPadding = innerPadding,
                onOpenMedia = { currentScreen = TallerScreen.Media },
                onOpenMap = { currentScreen = TallerScreen.Map }
            )

            TallerScreen.Media -> MediaScreen(modifier = Modifier.padding(innerPadding))
            TallerScreen.Map -> MapScreen(modifier = Modifier.padding(innerPadding))
        }
    }
}

@Composable
private fun HomeScreen(
    innerPadding: PaddingValues,
    onOpenMedia: () -> Unit,
    onOpenMap: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Taller 2 - Permisos, localización y mapas",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Selecciona una funcionalidad para probar cámara, galería, ubicación, geocoder y mapa en Compose.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        HomeActionCard(
            title = "Multimedia",
            description = "Tomar o seleccionar fotos y videos desde la galería o la cámara.",
            icon = {
                Icon(
                    imageVector = Icons.Default.PhotoCamera,
                    contentDescription = null,
                    modifier = Modifier.size(52.dp)
                )
            },
            onClick = onOpenMedia
        )

        HomeActionCard(
            title = "Mapa y localización",
            description = "Ver tu ubicación actual, cambiar estilo según la luz y buscar direcciones.",
            icon = {
                Icon(
                    imageVector = Icons.Default.Map,
                    contentDescription = null,
                    modifier = Modifier.size(52.dp)
                )
            },
            onClick = onOpenMap
        )
    }
}

@Composable
private fun HomeActionCard(
    title: String,
    description: String,
    icon: @Composable () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                icon()
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Button(onClick = onClick) {
                Text(text = "Abrir")
            }
        }
    }
}

