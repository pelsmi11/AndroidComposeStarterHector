# Documentación Base para Proyecto Android (Kotlin + Jetpack Compose)

A continuación, se presenta una **documentación** base para un proyecto Android con **Kotlin** y **Jetpack Compose**, bajo la arquitectura **MVVM** y un enfoque de **módulos (features)**. Esta guía servirá como referencia para recordar cómo estructurar el proyecto, cómo consumir tu backend para **login** y **crear usuario**, gestionar la **navegación** y manejar estados y validaciones en formularios.

---

## Objetivo General

Construir una aplicación que:

1. Tenga **módulos separados** (por ejemplo, `login`, `register`, etc.) dentro de una estructura MVVM.
2. Implemente **formularios** con validaciones (para login y registro).
3. **Consuma** un backend existente (la API provista por:  
   `https://angeles-back-sistem-production.up.railway.app`)
4. Gestione la **navegación** y proteja pantallas privadas con un **token JWT** (o validación de usuario).
5. Maneje un **estado** compartido (por ejemplo, un texto que se refleje en otra pantalla).

---

## Estructura del Proyecto

La idea es tener un **módulo** base de la aplicación (`app`) y en su interior, organizar nuestro código por “features” (o módulos lógicos). Dentro de cada feature tendremos la división propia de MVVM (data, domain, presentation).

Por ejemplo, una **estructura** propuesta podría verse así:

com.example.myapp
├── core
│   ├── data
│   ├── domain
│   └── ui
├── feature
│   ├── login
│   │   ├── data
│   │   │   ├── LoginRepository.kt
│   │   │   └── … (dto, service, etc.)
│   │   ├── domain
│   │   │   └── LoginUseCase.kt (opcional, si usas casos de uso)
│   │   └── presentation
│   │       ├── LoginScreen.kt
│   │       ├── LoginViewModel.kt
│   │       └── …
│   ├── register
│   │   ├── data
│   │   ├── domain
│   │   └── presentation
│   └── home
│       ├── data
│       ├── domain
│       └── presentation
├── navigation
│   └── AppNavHost.kt
├── di
│   └── AppModule.kt
├── MainActivity.kt
└── MyApplication.kt (si lo requieres)

> **Nota**: Esta estructura es orientativa. Puedes ajustarla según tus gustos o necesidades. Lo importante es la **separación por features** y la **arquitectura MVVM**.

---

## 1. Arquitectura MVVM (Enfoque Módulos)

### a) data

- Aquí irán las clases que se encargan de consumir tu API, almacenar los datos, etc.
- Por ejemplo, un `LoginRepository` que haga las llamadas a `Retrofit` para `/auth/login` y `/auth/get-valid-user`.

### b) domain

- Opcionalmente, si quieres usar casos de uso, colocarías clases como `LoginUseCase`, `CreateUserUseCase`, etc.
- También se podrían ubicar aquí modelos de dominio (datos ya “procesados” o relevantes para la lógica de negocio).

### c) presentation

- Incluye los **ViewModels** y las **pantallas** (`.kt` con Composables).
- Ejemplo: `LoginViewModel.kt` y `LoginScreen.kt`.

---

## 2. Formularios y Validaciones

Necesitamos **2 formularios** principales:

1. **Login** (correo y contraseña)

   - Validar que el correo tenga un formato correcto (opcional).
   - Validar que la contraseña no esté vacía.

2. **Registro (Crear usuario)**
   - Campos: nombre, correo, contraseña, etc. (puedes elegir los que requiera tu API).
   - Validaciones: correo válido, contraseña con longitud mínima, confirmación de contraseña, etc.

### Manejo de estados y validación en Compose

Para cada formulario, usaremos un **ViewModel** que exponga un `StateFlow` o `MutableStateFlow` con el “estado del formulario” (por ejemplo, `FormState`). Cada campo (email, password, etc.) será parte de ese estado, y el Composable observará estos valores con `collectAsState()`.

**Ejemplo de un ViewModel simplificado (LoginViewModel):**

```kotlin
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun onEmailChanged(newEmail: String) {
        _uiState.value = _uiState.value.copy(email = newEmail)
    }

    fun onPasswordChanged(newPassword: String) {
        _uiState.value = _uiState.value.copy(password = newPassword)
    }

    fun login() {
        val current = _uiState.value
        // Validaciones
        if (!current.email.contains("@")) {
            // Podrías mostrar un error
            _uiState.value = current.copy(error = "Correo inválido")
            return
        }
        if (current.password.isEmpty()) {
            _uiState.value = current.copy(error = "La contraseña está vacía")
            return
        }

        viewModelScope.launch {
            try {
                val result = loginRepository.login(current.email, current.password)
                if (result.isSuccessful) {
                    val token = result.body()?.token
                    // Guardar token y navegar
                    _uiState.value = current.copy(isLoggedIn = true, error = null)
                } else {
                    _uiState.value = current.copy(error = "Error en login")
                }
            } catch (e: Exception) {
                _uiState.value = current.copy(error = e.message)
            }
        }
    }
}

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoggedIn: Boolean = false,
    val error: String? = null
)
```

Ejemplo de Composable (LoginScreen):

```kotlin
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        TextField(
            value = uiState.email,
            onValueChange = { viewModel.onEmailChanged(it) },
            label = { Text("Correo") }
        )
        TextField(
            value = uiState.password,
            onValueChange = { viewModel.onPasswordChanged(it) },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation()
        )

        if (uiState.error != null) {
            Text(uiState.error, color = Color.Red)
        }

        Button(onClick = { viewModel.login() }) {
            Text("Iniciar Sesión")
        }

        // Navegar si el login fue exitoso
        if (uiState.isLoggedIn) {
            onLoginSuccess()
        }
    }
}
```

Lo mismo se aplicaría para el formulario de registro (RegisterViewModel, RegisterScreen, validaciones de campos, etc.).

3. Navegación y Rutas Protegidas

Usaremos la navegación de Compose (NavController / NavHost).
•	Vamos a tener rutas como login, register y home.
•	Para proteger las pantallas de home (o cualquier ruta interna), realizaremos una verificación del token (o un endpoint del backend).

Por ejemplo, tu backend provee la ruta:

GET /auth/get-valid-user

que verifica si el usuario con cierto JWT es válido.

Flujo de navegación sugerido
1.	Splash o pantalla inicial
•	Comprueba si tienes un token guardado (por ejemplo, en DataStore).
•	Llama a get-valid-user; si es válido, navega directo a home. Si no, navega a login.
2.	Login
•	Al iniciar sesión, se guarda el token. Luego, navegas a home.
3.	Home (pantalla protegida)
•	Si por alguna razón el token expira o el backend responde 401, vuelves a login.

Ejemplo de NavHost:

```kotlin
@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.Splash.route
    ) {
        composable(Routes.Splash.route) {
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate(Routes.Login.route) {
                        popUpTo(Routes.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(Routes.Home.route) {
                        popUpTo(Routes.Splash.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.Login.route) {
            LoginScreen(onLoginSuccess = {
                navController.navigate(Routes.Home.route) {
                    popUpTo(Routes.Login.route) { inclusive = true }
                }
            })
        }
        composable(Routes.Register.route) {
            RegisterScreen(onRegisterSuccess = {
                // similar a login
            })
        }
        composable(Routes.Home.route) {
            HomeScreen()
        }
    }
}
```

	Nota: Las rutas (Routes.Splash.route, etc.) las puedes definir en un archivo Routes.kt o en un objeto Routes para mantenerlas organizadas.

4. Estado Compartido entre Pantallas

Si queremos, por ejemplo, que un texto modificado en una pantalla se refleje en otra:
1.	Podemos usar un ViewModel “global” (por ejemplo, un MainViewModel o un SharedViewModel) que se instancie a nivel de la MainActivity o a nivel de la ruta principal.
2.	Al modificar el texto en Pantalla A, se actualiza el StateFlow del ViewModel global.
3.	Pantalla B observa ese StateFlow y se actualiza en tiempo real.

Ejemplo (muy simplificado):

```kotlin
@HiltViewModel
class SharedViewModel @Inject constructor(): ViewModel() {
    private val _sharedText = MutableStateFlow("")
    val sharedText: StateFlow<String> = _sharedText

    fun updateText(newText: String) {
        _sharedText.value = newText
    }
}
```
	•	En la Pantalla A (ej. ScreenA):

```kotlin
@Composable
fun ScreenA(
    sharedViewModel: SharedViewModel = hiltViewModel()
) {
    val textState by sharedViewModel.sharedText.collectAsState()

    TextField(
        value = textState,
        onValueChange = { sharedViewModel.updateText(it) }
    )
}
```

	•	En la Pantalla B (ej. ScreenB):

```kotlin
@Composable
fun ScreenB(
    sharedViewModel: SharedViewModel = hiltViewModel()
) {
    val textState by sharedViewModel.sharedText.collectAsState()
    Text("Texto actual: $textState")
}
```


De esa forma, si en la Pantalla A se cambia el texto, en la Pantalla B se ve inmediatamente reflejado.

---

## Pasos Recomendados para Empezar el Proyecto

1. **Crear el proyecto base en Android Studio** con las siguientes opciones:
   - Lenguaje: Kotlin
   - Compose Enabled: Sí
   - Activity: Empty Compose Activity (o la opción predeterminada para Compose).

2. **Configurar la inyección de dependencias (Hilt)** (opcional, pero muy recomendado).
   - Añadir Hilt al `build.gradle` principal.
   - Crear tu clase `MyApplication : Application` con `@HiltAndroidApp`.
   - Configurar `AndroidManifest.xml` para apuntar a `MyApplication`.

3. **Crear paquetes** (`feature/login`, `feature/register`, `feature/home`, etc.) cada uno con su `data`, `domain`, `presentation`.

4. **Configurar Retrofit** para consumir tu backend:
   - Interfaz `ApiService` con endpoints (`login`, `register`, `get-valid-user`, etc.).
   - Repositorios que usen `ApiService`.

5. **Crear los ViewModels** y **pantallas** de Compose para `LoginScreen` y `RegisterScreen`.  
   Implementar las validaciones y la lógica de consumo de la API.

6. **Navegación**:
   - Crear tu `NavHost` con las rutas (`Splash`, `Login`, `Register`, `Home`), configurarlo en `MainActivity`.
   - Asegurar la protección de rutas mediante verificación del token (en `SplashScreen` o interceptando un `401`).

7. **State Compartido** (opcional al inicio):
   - Puedes empezar con un `ViewModel` normal para cada pantalla.
   - Luego, si deseas estado global, creas un `SharedViewModel` y lo usas en varias pantallas.

---

## Resumen

Este **ejercicio-proyecto** servirá como **plantilla** para practicar:

- Arquitectura **MVVM** con un enfoque de **módulos** (por feature).
- Formularios de **login** y **registro**, validaciones y manejo de errores en Compose.
- **Navegación** entre pantallas y protección de rutas mediante token JWT (verificando contra el backend).
- **Estado** en Compose (cómo reflejar cambios en tiempo real entre pantallas).

### Pasos principales para recordarlos en el futuro

1. **Estructurar** por features (login, register, etc.).
2. **Crear** ViewModels y exponer estados (`StateFlow`).
3. **Diseñar** pantallas en Compose que observen esos estados (`collectAsState()`).
4. **Conectar** con el backend (Retrofit + Repositorios).
5. **Controlar** la navegación y la protección de rutas (verificación de token / endpoint).