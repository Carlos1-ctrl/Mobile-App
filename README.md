# Mobile App - Random User

Aplicación Android desarrollada en Kotlin con Jetpack Compose que consume la API pública de [randomuser.me](https://randomuser.me) para mostrar un listado de usuarios aleatorios con sus detalles.

## Pantallas

### Pantalla 1 - Lista de usuarios
- Listado de usuarios con Nombre, Email y Género
- Filtros por género (Hombres / Mujeres) y botón de Reset
- Sección de favoritos persistentes (se mantienen al cerrar la app)
- Eliminación individual de usuarios con confirmación
- Paginación automática al llegar al final de la lista

### Pantalla 2 - Detalle de usuario
- Imagen, Dirección y Teléfono del usuario
- Distancia en KM entre el dispositivo y la ubicación del usuario
- Ver ubicación del usuario en Google Maps
- Compartir contacto por WhatsApp (Nombre, Teléfono y Email)
- Guardar contacto en la agenda del dispositivo
- Exportar información a PDF en la carpeta de Descargas
- Tomar fotografía y mostrarla (persistente por usuario)
- Formulario de credenciales con validación (correo y contraseña)

## Tecnologías

- **Kotlin** — Lenguaje principal
- **Jetpack Compose** — UI declarativa
- **MVVM** — Patrón de arquitectura
- **Retrofit + Gson** — Consumo de API REST
- **DataStore Preferences** — Persistencia de favoritos y fotos
- **ViewModel + ViewModelFactory** — Manejo de estado
- **Coil** — Carga de imágenes
- **Google Play Services Location** — Ubicación del dispositivo
- **Material 3** — Diseño con dark mode

## API

Documentación: [https://randomuser.me/documentation](https://randomuser.me/documentation)

- `GET https://randomuser.me/api/?results=10` — Lista de usuarios
- `GET https://randomuser.me/api/?results=10&gender=male` — Filtro hombres
- `GET https://randomuser.me/api/?results=10&gender=female` — Filtro mujeres

## Permisos requeridos

- `CAMERA` — Tomar fotografías
- `ACCESS_FINE_LOCATION` — Calcular distancia al usuario
- `WRITE_CONTACTS` / `READ_CONTACTS` — Guardar contactos en la agenda
- `INTERNET` — Consumir la API

## Cómo correr el proyecto

1. Clona el repositorio
```bash
   git clone https://github.com/Carlos1-ctrl/Mobile-App.git
```
2. Ábrelo en **Android Studio**
3. Sincroniza el proyecto con Gradle
4. Corre en un dispositivo o emulador con **Android 8+**

## Estructura del proyecto
```
app/
├── data/
│   ├── api/          # Retrofit e interfaz de la API
│   ├── datastore/    # Persistencia con DataStore
│   ├── model/        # Modelos de datos
│   └── repository/   # Repositorio de usuarios
├── ui/
│   ├── components/   # Componentes reutilizables
│   ├── navegation/   # Configuración de navegación
│   ├── screens/      # Pantallas de la app
│   └── theme/        # Colores y tema dark mode
├── utils/            # Funciones utilitarias
└── viewmodel/        # ViewModels y Factory
```