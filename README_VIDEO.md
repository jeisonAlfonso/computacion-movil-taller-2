# Guion exacto para el video de entrega

Este archivo contiene un guion listo para leer durante la grabación del video del taller.
La idea es que puedas seguirlo casi palabra por palabra mientras muestras la app y el código.

---

## Estructura sugerida del video

- Duración objetivo: entre 5 y 6 minutos.
- Orden recomendado:
  1. Presentación
  2. Pantalla principal
  3. Multimedia
  4. Mapa y localización
  5. Código
  6. APK y cierre

---

# Guion exacto

## 1. Presentación

### Qué mostrar
- La app abierta o Android Studio con el proyecto.

### Qué decir

> Hola. En este video presento el Taller 2 de Android desarrollado con Jetpack Compose. En esta aplicación se implementan permisos, uso de cámara, selección de contenido desde galería, reproducción de video, localización actual, mapas de Google, geocoder, cambio de estilo del mapa según la luminosidad y trazado de rutas con polyline.

Si el trabajo es en pareja, agrega esta línea:

> Este trabajo fue realizado por [NOMBRE 1] y [NOMBRE 2].

---

## 2. Pantalla principal

### Qué mostrar
- Inicia la app.
- Muestra la pantalla principal con los dos botones.

### Qué decir

> Esta es la pantalla principal de la aplicación. Aquí se encuentran los dos accesos principales del taller. El primero corresponde a la funcionalidad de multimedia, donde el usuario puede tomar o seleccionar fotos y videos. El segundo corresponde a la funcionalidad de mapa y localización, donde se muestra la ubicación actual del usuario y varias interacciones con el mapa.

---

## 3. Pantalla de multimedia

### Qué mostrar
- Entra al botón de multimedia.
- Muestra el switch entre foto y video.

### Qué decir

> En esta pantalla se implementó el manejo de contenido multimedia. El usuario puede cambiar entre modo foto y modo video usando este switch. Dependiendo de la opción seleccionada, se habilitan las acciones para capturar desde la cámara del sistema o seleccionar desde la galería.

---

## 4. Tomar foto con cámara

### Qué mostrar
- Deja el switch en foto.
- Pulsa `Tomar Foto`.
- Muestra la solicitud de permiso de cámara.
- Toma una foto.
- Regresa a la app y muestra la vista previa.

### Qué decir

> En modo foto, al presionar el botón Tomar Foto, la aplicación solicita permiso de cámara cuando es necesario. Después se abre la aplicación de cámara del dispositivo. Una vez el usuario toma la fotografía correctamente, la imagen se muestra dentro de la aplicación, centrada y con un tamaño fijo, sin depender del tamaño original del archivo.

---

## 5. Seleccionar foto desde galería

### Qué mostrar
- Pulsa `Seleccionar Foto`.
- Elige una imagen desde galería.
- Muestra la vista previa.

### Qué decir

> También se implementó la selección de imágenes desde la galería. Al presionar Seleccionar Foto, el usuario puede escoger una imagen existente y el resultado se muestra en esta misma zona de vista previa dentro de la aplicación.

---

## 6. Grabar video con cámara

### Qué mostrar
- Cambia el switch a video.
- Pulsa `Grabar Video`.
- Muestra permiso si aparece.
- Graba un video corto.
- Regresa a la app.
- Muestra el video reproduciéndose.

### Qué decir

> Ahora cambio al modo video. En este caso, al presionar Grabar Video, la aplicación solicita el permiso de cámara y abre la aplicación de cámara del sistema para grabar un video. Cuando la grabación finaliza correctamente, el video queda cargado en la aplicación y puede visualizarse directamente dentro de la pantalla.

---

## 7. Seleccionar video desde galería

### Qué mostrar
- Pulsa `Seleccionar Video`.
- Elige un video desde galería.
- Muestra la reproducción.

### Qué decir

> Además, en modo video también se permite seleccionar un archivo existente desde la galería. Al elegir el video, este se muestra en la vista previa dentro de la aplicación, cumpliendo con el requerimiento de cargar contenido multimedia tanto desde la cámara como desde el almacenamiento del dispositivo.

---

## 8. Ir a la pantalla de mapas

### Qué mostrar
- Regresa al inicio.
- Entra al botón de mapa.
- Si aparecen permisos de ubicación, acéptalos.

### Qué decir

> Ahora voy a mostrar la segunda funcionalidad principal del taller, que corresponde al manejo de mapa, permisos de ubicación, geocoder, sensor de luminosidad y trazado de rutas. Al ingresar a esta pantalla, la aplicación solicita los permisos de localización necesarios para acceder a la posición actual del usuario.

---

## 9. Marcador de ubicación actual

### Qué mostrar
- Muestra el mapa con el marcador actual.
- Si se ve el botón de mi ubicación, puedes usarlo.

### Qué decir

> Una vez concedido el permiso, el mapa muestra la localización actual del usuario mediante un marcador. Además, la cámara del mapa se mueve automáticamente a la posición actual para facilitar la visualización.

---

## 10. Cambio de estilo del mapa por luminosidad

### Qué mostrar
- Señala el texto donde se indica si el estilo está claro u oscuro.
- Si puedes, demuestra el cambio con el sensor.

### Qué decir

> Esta pantalla también reacciona al sensor de luminosidad del dispositivo. Cuando la aplicación detecta baja luminosidad, el mapa se presenta con estilo oscuro. Cuando detecta alta luminosidad, cambia a estilo claro. De esta forma se cumple el requisito de adaptar la apariencia del mapa según las condiciones de luz.

Si no logras mostrar el cambio en vivo, usa esta frase:

> En este dispositivo o emulador la simulación del sensor puede ser limitada, pero la lógica ya está implementada y aquí se puede ver el estado que determina qué estilo se aplica en el mapa.

---

## 11. Polyline del recorrido del usuario

### Qué mostrar
- Si estás en emulador, simula movimiento.
- Si estás en celular, muestra un pequeño cambio de posición.
- Señala la línea de recorrido.

### Qué decir

> Cada vez que cambia la ubicación del usuario, la aplicación registra el nuevo punto y dibuja una polyline sobre el mapa. Esa línea representa la ruta de desplazamiento del usuario y se va construyendo dinámicamente a medida que se reciben actualizaciones de localización.

---

## 12. Búsqueda por texto con Geocoder

### Qué mostrar
- En el cuadro de texto escribe, por ejemplo, `Universidad Javeriana`.
- Pulsa `Ir`.
- Muestra el nuevo marcador y el movimiento de cámara.

### Qué decir

> En esta parte se implementó la búsqueda de direcciones en texto claro usando Geocoder. Por ejemplo, voy a escribir Universidad Javeriana. Al confirmar la búsqueda, el texto se convierte en coordenadas geográficas, se crea un marcador en la ubicación encontrada y la cámara se mueve automáticamente hacia ese punto.

---

## 13. Long click con geocoder inverso

### Qué mostrar
- Haz una pulsación larga sobre cualquier punto del mapa.
- Muestra el nuevo marcador.

### Qué decir

> También se implementó el evento de pulsación larga sobre el mapa. Cuando hago un long click sobre cualquier posición, la aplicación crea un marcador en ese punto. El título de ese marcador se obtiene mediante geocoder inverso, es decir, convirtiendo las coordenadas del punto en una dirección legible.

---

## 14. Punto extra o bono

### Qué mostrar
- Señala la línea directa entre la ubicación actual y el último destino buscado o marcado.

### Qué decir

> Adicionalmente, se implementó una ruta directa entre la localización actual del usuario y el último destino seleccionado mediante búsqueda o mediante un long click en el mapa. Esta funcionalidad aporta al punto extra planteado en el taller.

---

## 15. Mostrar el código

### Qué mostrar
Abre estos archivos en Android Studio:
- `app/src/main/java/com/example/taller2/MainActivity.kt`
- `app/src/main/java/com/example/taller2/ui/Taller2App.kt`
- `app/src/main/java/com/example/taller2/ui/MediaScreen.kt`
- `app/src/main/java/com/example/taller2/ui/MapScreen.kt`
- `app/src/main/java/com/example/taller2/location/GeocoderUtils.kt`
- `app/src/main/AndroidManifest.xml`

### Qué decir

> Ahora voy a mostrar brevemente las partes principales del código. En MainActivity y Taller2App se encuentra la estructura principal de la aplicación y la navegación entre pantallas. En MediaScreen está implementado el manejo de cámara, galería, foto, video y vista previa. En MapScreen se encuentra la lógica de permisos de ubicación, mapa, sensor de luz, polyline, búsqueda por texto y marcadores por long click. En GeocoderUtils se encapsula la lógica de geocoder y geocoder inverso. Finalmente, en AndroidManifest se definen los permisos requeridos por la aplicación.

---

## 16. Mostrar el APK

### Qué mostrar
- Muestra la ruta del APK generado.
- Si quieres, abre la carpeta `app/build/outputs/apk/debug/`.

### Qué decir

> Finalmente, este es el APK generado por la aplicación, ubicado en la ruta app build outputs apk debug app-debug.apk. Con esto quedan incluidos los entregables principales del taller junto con el código fuente del proyecto.

---

## 17. Cierre

### Qué decir

> Con esto se demuestra el cumplimiento de los requerimientos del Taller 2. Muchas gracias.

---

# Versión corta por si quieres leer todo seguido

> Hola. En este video presento el Taller 2 de Android desarrollado con Jetpack Compose. En esta aplicación se implementan permisos, uso de cámara, selección de contenido desde galería, reproducción de video, localización actual, mapas de Google, geocoder, cambio de estilo del mapa según la luminosidad y trazado de rutas con polyline.
>
> Esta es la pantalla principal de la aplicación. Aquí se encuentran los dos accesos principales del taller. El primero corresponde a la funcionalidad de multimedia, donde el usuario puede tomar o seleccionar fotos y videos. El segundo corresponde a la funcionalidad de mapa y localización, donde se muestra la ubicación actual del usuario y varias interacciones con el mapa.
>
> En esta pantalla se implementó el manejo de contenido multimedia. El usuario puede cambiar entre modo foto y modo video usando este switch. Dependiendo de la opción seleccionada, se habilitan las acciones para capturar desde la cámara del sistema o seleccionar desde la galería.
>
> En modo foto, al presionar el botón Tomar Foto, la aplicación solicita permiso de cámara cuando es necesario. Después se abre la aplicación de cámara del dispositivo. Una vez el usuario toma la fotografía correctamente, la imagen se muestra dentro de la aplicación, centrada y con un tamaño fijo, sin depender del tamaño original del archivo.
>
> También se implementó la selección de imágenes desde la galería. Al presionar Seleccionar Foto, el usuario puede escoger una imagen existente y el resultado se muestra en esta misma zona de vista previa dentro de la aplicación.
>
> Ahora cambio al modo video. En este caso, al presionar Grabar Video, la aplicación solicita el permiso de cámara y abre la aplicación de cámara del sistema para grabar un video. Cuando la grabación finaliza correctamente, el video queda cargado en la aplicación y puede visualizarse directamente dentro de la pantalla.
>
> Además, en modo video también se permite seleccionar un archivo existente desde la galería. Al elegir el video, este se muestra en la vista previa dentro de la aplicación, cumpliendo con el requerimiento de cargar contenido multimedia tanto desde la cámara como desde el almacenamiento del dispositivo.
>
> Ahora voy a mostrar la segunda funcionalidad principal del taller, que corresponde al manejo de mapa, permisos de ubicación, geocoder, sensor de luminosidad y trazado de rutas. Al ingresar a esta pantalla, la aplicación solicita los permisos de localización necesarios para acceder a la posición actual del usuario.
>
> Una vez concedido el permiso, el mapa muestra la localización actual del usuario mediante un marcador. Además, la cámara del mapa se mueve automáticamente a la posición actual para facilitar la visualización.
>
> Esta pantalla también reacciona al sensor de luminosidad del dispositivo. Cuando la aplicación detecta baja luminosidad, el mapa se presenta con estilo oscuro. Cuando detecta alta luminosidad, cambia a estilo claro. De esta forma se cumple el requisito de adaptar la apariencia del mapa según las condiciones de luz.
>
> Cada vez que cambia la ubicación del usuario, la aplicación registra el nuevo punto y dibuja una polyline sobre el mapa. Esa línea representa la ruta de desplazamiento del usuario y se va construyendo dinámicamente a medida que se reciben actualizaciones de localización.
>
> En esta parte se implementó la búsqueda de direcciones en texto claro usando Geocoder. Por ejemplo, voy a escribir Universidad Javeriana. Al confirmar la búsqueda, el texto se convierte en coordenadas geográficas, se crea un marcador en la ubicación encontrada y la cámara se mueve automáticamente hacia ese punto.
>
> También se implementó el evento de pulsación larga sobre el mapa. Cuando hago un long click sobre cualquier posición, la aplicación crea un marcador en ese punto. El título de ese marcador se obtiene mediante geocoder inverso, es decir, convirtiendo las coordenadas del punto en una dirección legible.
>
> Adicionalmente, se implementó una ruta directa entre la localización actual del usuario y el último destino seleccionado mediante búsqueda o mediante un long click en el mapa. Esta funcionalidad aporta al punto extra planteado en el taller.
>
> Ahora voy a mostrar brevemente las partes principales del código. En MainActivity y Taller2App se encuentra la estructura principal de la aplicación y la navegación entre pantallas. En MediaScreen está implementado el manejo de cámara, galería, foto, video y vista previa. En MapScreen se encuentra la lógica de permisos de ubicación, mapa, sensor de luz, polyline, búsqueda por texto y marcadores por long click. En GeocoderUtils se encapsula la lógica de geocoder y geocoder inverso. Finalmente, en AndroidManifest se definen los permisos requeridos por la aplicación.
>
> Finalmente, este es el APK generado por la aplicación, ubicado en la ruta app build outputs apk debug app-debug.apk. Con esto quedan incluidos los entregables principales del taller junto con el código fuente del proyecto.
>
> Con esto se demuestra el cumplimiento de los requerimientos del Taller 2. Muchas gracias.

---

# Checklist antes de grabar

- Tener configurada la API key de Google Maps.
- Tener una foto en galería.
- Tener un video en galería.
- Tener permisos limpios si quieres que se vea la solicitud.
- Tener lista una búsqueda como `Universidad Javeriana`.
- Si usas emulador, dejar preparada la simulación de ubicación.
- Verificar que el APK exista en `app/build/outputs/apk/debug/app-debug.apk`.

