# Taller 2 - Permisos, localización y mapas en Android con Compose

Proyecto Android con Jetpack Compose que implementa:

- Pantalla principal con dos accesos: multimedia y mapas.
- Captura de foto con cámara del sistema.
- Grabación de video con cámara del sistema.
- Selección de foto y video desde galería.
- Vista previa fija centrada para imagen o video.
- Mapa con marcador de ubicación actual.
- Cambio de estilo claro/oscuro según sensor de luminosidad.
- Polyline con la ruta del desplazamiento del usuario.
- Búsqueda de dirección por texto usando `Geocoder`.
- Creación de marcadores por `LongClick` con geocoder inverso.
- Ruta directa opcional entre la ubicación actual y el último destino seleccionado.

## Configuración importante

Para que Google Maps funcione en un dispositivo o emulador, reemplaza el valor de `google_maps_key` en:

- `app/src/main/res/values/strings.xml`

por una API key válida de Google Maps SDK for Android.

## Compilación

```powershell
.\gradlew.bat assembleDebug
.\gradlew.bat testDebugUnitTest
```

## APK generado

Después de compilar, el APK debug queda en:

- `app/build/outputs/apk/debug/app-debug.apk`

## Notas

- La pantalla de multimedia solicita permiso de cámara solo cuando es necesario.
- La galería usa selección de contenido del sistema.
- La pantalla de mapas solicita permisos de ubicación para mostrar la posición actual y el recorrido.

