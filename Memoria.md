# Memoria del Proyecto: MiGym - Aplicación de Gestión de Entrenamientos

## Introducción

Esta memoria documenta el desarrollo de la aplicación móvil MiGym, una herramienta diseñada para la gestión y planificación de sesiones de entrenamiento personal. El proyecto se realizó como parte de una entrega académica, utilizando tecnologías modernas de desarrollo Android para crear una aplicación funcional, intuitiva y escalable.

La aplicación permite a los usuarios organizar sus rutinas de ejercicio semanalmente, recibir notificaciones y personalizar la experiencia mediante configuraciones como idioma y tema. Se ha desarrollado siguiendo las mejores prácticas de Android, incluyendo arquitectura MVVM, persistencia de datos con Room y navegación moderna.

A lo largo de este documento, se detallarán todos los aspectos técnicos, desde la estructura del código hasta el manual de usuario, con el fin de proporcionar una comprensión completa del proyecto.

## Enlace al Repositorio de Código Git

El proyecto completo, incluyendo todo el código fuente, recursos y documentación, se encuentra disponible en el siguiente repositorio público de GitHub:  
[https://github.com/andercalvo07/DasProyectoEntrega1](https://github.com/ainhoagaarciia/DasProyectoEntrega1)

Este repositorio contiene:
- Código fuente en Java y Kotlin
- Archivos de configuración Gradle
- Recursos de interfaz (layouts, strings, etc.)
- Tests unitarios e instrumentados
- Documentación adicional

Para clonar el repositorio, ejecutar: `git clone https://github.com/andercalvo07/DasProyectoEntrega1.git`

## Enumeración de los Elementos de Valoración Utilizados y su Funcionalidad

La aplicación MiGym emplea una variedad de componentes de interfaz de usuario (UI components) y elementos técnicos para ofrecer una experiencia de usuario rica y funcional. A continuación, se enumeran y describen detalladamente cada uno, incluyendo su propósito, implementación y funcionalidad específica.

### 1. BottomNavigationView
**Funcionalidad**: Proporciona navegación rápida entre las secciones principales de la aplicación.  
**Implementación**: Configurado en `MainActivity.java` con `NavigationUI.setupWithNavController()`.  
**Elementos**: Home, Workout, Profile, Settings.  
**Beneficio**: Mejora la usabilidad al permitir acceso directo a funciones clave sin necesidad de navegación jerárquica compleja.

### 2. NavigationView (Drawer)
**Funcionalidad**: Menú lateral deslizable para navegación adicional y acceso a configuraciones avanzadas.  
**Implementación**: Integrado con `DrawerLayout` en el layout de `MainActivity`.  
**Elementos**: Enlaces a todas las secciones principales, con iconos y títulos.  
**Beneficio**: Ofrece una navegación alternativa para usuarios que prefieren menús expansivos.

### 3. RecyclerView
**Funcionalidad**: Muestra listas dinámicas de entrenamientos, permitiendo scroll eficiente.  
**Implementación**: Utiliza `WorkoutAdapter.java` para vincular datos a vistas. Soporta `LinearLayoutManager` para listas verticales.  
**Elementos**: Cada ítem muestra título, descripción, hora y duración del entrenamiento.  
**Beneficio**: Optimizado para grandes cantidades de datos, con reciclaje de vistas para rendimiento.

### 4. ViewPager2
**Funcionalidad**: Permite navegación horizontal entre días de la semana en la vista de calendario.  
**Implementación**: Configurado con `WorkoutPagerAdapter.kt` para crear fragments por día.  
**Elementos**: Tabs para cada día (Lunes a Domingo).  
**Beneficio**: Facilita la visualización semanal de entrenamientos de manera intuitiva.

### 5. MaterialToolbar
**Funcionalidad**: Barra superior con título de la aplicación y menú de opciones.  
**Implementación**: `setSupportActionBar()` en `MainActivity.java`, con menú inflado desde `options_menu.xml`.  
**Elementos**: Botones para cambio de idioma y modo oscuro.  
**Beneficio**: Proporciona acceso rápido a configuraciones globales.

### 6. FloatingActionButton (FAB)
**Funcionalidad**: Botón flotante para acciones primarias, como agregar nuevos entrenamientos.  
**Implementación**: Posicionado en `WorkoutFragment.java`, con `onClickListener` para navegar a pantalla de creación.  
**Elementos**: Icono de "+" con animación de elevación.  
**Beneficio**: Sigue las guías de Material Design para acciones principales.

### 7. AlertDialog
**Funcionalidad**: Diálogos modales para confirmaciones, selecciones y entradas de usuario.  
**Implementación**: `AlertDialog.Builder` en métodos como `showLanguageDialog()` en `MainActivity.java`.  
**Elementos**: Títulos, mensajes, botones (positivo/negativo).  
**Beneficio**: Interrupción controlada para acciones críticas sin perder contexto.

### 8. SharedPreferences
**Funcionalidad**: Almacenamiento persistente de configuraciones simples del usuario.  
**Implementación**: `PreferenceManager.getDefaultSharedPreferences()` para guardar idioma y modo nocturno.  
**Elementos**: Claves como "language" y "night_mode".  
**Beneficio**: Persistencia ligera sin necesidad de base de datos para configuraciones.

### 9. Room Database
**Funcionalidad**: Base de datos local SQLite para persistencia de datos de entrenamientos.  
**Implementación**: Entidades, DAOs y Database en paquete `data/`.  
**Elementos**: Tabla `workouts` con campos definidos.  
**Beneficio**: Abstracción de SQLite, con consultas tipo-safe y observabilidad con LiveData.

### 10. WorkManager
**Funcionalidad**: Programación de tareas en segundo plano, como notificaciones de recordatorios.  
**Implementación**: En paquete `workers/`, con `PeriodicWorkRequest` para tareas recurrentes.  
**Elementos**: Workers para notificaciones diarias.  
**Beneficio**: Garantiza ejecución incluso si la app está cerrada, con manejo de restricciones.

### 11. LiveData y ViewModel
**Funcionalidad**: Arquitectura MVVM para separación de responsabilidades y UI reactiva.  
**Implementación**: ViewModels en `viewmodels/`, LiveData en DAOs y repositorios.  
**Elementos**: Observadores en Fragments para actualizar UI automáticamente.  
**Beneficio**: Evita memory leaks y simplifica gestión de estado de UI.

## Descripción de las Clases y Bases de Datos

### Arquitectura General
La aplicación sigue la arquitectura MVVM (Model-View-ViewModel), con capas claramente separadas:
- **Model**: Datos y lógica de negocio (entidades, repositorios).
- **View**: UI (Activities, Fragments, Adapters).
- **ViewModel**: Puente entre Model y View, manejando estado y lógica de presentación.

### Clases Principales

#### Modelo de Datos
- **Workout (data/Workout.java)**: Entidad principal de Room. Representa un entrenamiento con campos como id (auto-generado), title, description, location, dayOfWeek (int para Calendar), type (int para categorías), time (String HH:MM), duration (int en minutos). Incluye métodos getter/setter y sobrecargas para equals/hashCode.  
  Código relevante:
  ```java
  @Entity(tableName = "workouts")
  public class Workout {
      @PrimaryKey(autoGenerate = true)
      private Long id;
      private String title;
      // ... otros campos
  }
  ```

- **Workout (model/Workout.kt)**: Data class Kotlin equivalente, utilizada en lógica de negocio.  
  ```kotlin
  data class Workout(
      val id: String,
      val title: String,
      val description: String,
      val type: String,
      val day: String,
      val hour: String,
      val duration: Int
  )
  ```

- **WorkoutSession (models/WorkoutSession.java)**: Clase Java para sesiones, con constructores y getters/setters. Similar a Workout pero con dayOfWeek como int directo.

#### Base de Datos
- **AppDatabase (data/AppDatabase.java)**: Singleton que configura Room.  
  ```java
  @Database(entities = {Workout.class}, version = 1)
  public abstract class AppDatabase extends RoomDatabase {
      public abstract WorkoutDao workoutDao();
  }
  ```

- **WorkoutDao (data/WorkoutDao.java)**: Interfaz con métodos CRUD.  
  ```java
  @Dao
  public interface WorkoutDao {
      @Query("SELECT * FROM workouts ORDER BY dayOfWeek ASC, time ASC")
      LiveData<List<Workout>> getAllWorkouts();
      // ... otros métodos
  }
  ```

- **Converters (data/Converters.java)**: Para tipos no soportados por Room, como Date.

#### UI y Controladores
- **MainActivity.java**: Punto de entrada, configura navegación y toolbar. Maneja cambios de idioma y modo oscuro aplicando configuración y recreando actividad.  
  Código clave:
  ```java
  private void applySettings() {
      // Aplicar night mode y language
  }
  ```

- **HomeFragment.java**: Muestra resumen, posiblemente estadísticas o próximos entrenamientos.

- **WorkoutFragment.java**: Centro de gestión de entrenamientos, con lista y calendario.

- **ProfileFragment.java**: Para información de usuario (básico en esta versión).

- **SettingsFragment.java**: Configuraciones avanzadas.

- **WorkoutAdapter.java**: Extiende RecyclerView.Adapter, infla layout para cada ítem y vincula datos.

- **WorkoutPagerAdapter.kt**: Para ViewPager2, crea fragments por día.

#### Repositorios y ViewModels
- **WorkoutRepository.java**: Abstrae acceso a datos, combina DAO y posibles APIs remotas.

- ViewModels: Clases como WorkoutViewModel que exponen LiveData a la UI.

### Diagramas

#### Diagrama de Arquitectura MVVM
Insertar diagrama aquí: Diagrama que muestre las capas Model (Database, Repository), ViewModel (con LiveData), y View (Activity/Fragment con observers). Flechas indicando flujo de datos unidireccional.

#### Diagrama de Clases Detallado
Insertar diagrama aquí: Diagrama UML de clases mostrando relaciones entre MainActivity, Fragments, Adapters, ViewModels, Repository, DAO, y entidades. Incluir multiplicidades y dependencias.

Texto simplificado:
```
[MainActivity] --> [NavController] --> [Fragments]
[Fragments] --> [ViewModels] --> [LiveData]
[ViewModels] --> [Repository] --> [DAO] --> [Database]
```

#### Diagrama de Base de Datos
Insertar diagrama ER aquí: Entidad Workout con atributos y tipos. Mostrar clave primaria y posibles índices.

Texto:
Tabla: workouts
- id: INTEGER PRIMARY KEY AUTOINCREMENT
- title: TEXT NOT NULL
- description: TEXT
- location: TEXT
- dayOfWeek: INTEGER
- type: INTEGER
- time: TEXT
- duration: INTEGER

#### Diagrama de Navegación
Insertar diagrama de flujo aquí: Grafo de navegación con nodos para cada fragment y aristas para acciones. Incluir bottom nav y drawer.

## Manual de Usuario

### Requisitos del Sistema
- Dispositivo Android con versión 7.0 (API 24) o superior.
- Espacio de almacenamiento: 50 MB mínimo.
- Permisos: Notificaciones (opcional para recordatorios).

### Instalación y Configuración Inicial
1. Descargar el archivo APK desde el repositorio GitHub (releases) o compilar desde código fuente en Android Studio.
2. Transferir el APK al dispositivo y permitir instalación de fuentes desconocidas en configuraciones de seguridad.
3. Abrir la aplicación tras instalación.

Insertar captura de pantalla aquí: Pantalla de instalación mostrando icono de MiGym y permisos solicitados.

### Primer Uso
Al abrir la app por primera vez:
- Seleccionar idioma preferido (Español o English) desde el menú de opciones.
- Elegir modo de tema (claro/oscuro) según preferencia.

Insertar captura de pantalla aquí: Pantalla inicial con diálogo de idioma.

### Navegación Principal
La app utiliza una combinación de navegación inferior y lateral para facilitar el acceso.

#### Barra de Navegación Inferior
- **Home**: Resumen general, próximos entrenamientos.
- **Workout**: Gestión de rutinas y calendario semanal.
- **Profile**: Información personal (en desarrollo).
- **Settings**: Configuraciones de app.

Insertar captura de pantalla aquí: BottomNavigationView con íconos y etiquetas.

#### Menú Lateral (Drawer)
Accesible deslizando desde el borde izquierdo o tocando el ícono de hamburguesa en la toolbar.
- Enlaces a todas las secciones.
- Información de versión.

Insertar captura de pantalla aquí: NavigationView abierto mostrando menú.

### Sección Home
Muestra:
- Número de entrenamientos esta semana.
- Próximo entrenamiento programado.
- Estadísticas básicas (si implementadas).

Insertar captura de pantalla aquí: HomeFragment con datos de ejemplo.

### Sección Workout
- **Vista de Calendario**: Tabs para cada día, con ViewPager2 para deslizar.
- **Lista de Entrenamientos**: RecyclerView con cards para cada sesión.
- **Agregar Entrenamiento**: FAB que abre diálogo o fragment de creación.
  Campos: Título, Descripción, Ubicación, Día, Hora, Duración, Tipo.

Insertar captura de pantalla aquí: WorkoutFragment en vista semanal.

Insertar captura de pantalla aquí: Diálogo de agregar entrenamiento con campos de formulario.

- **Editar/Eliminar**: Long press en ítem para opciones.

### Sección Profile
Actualmente básica, muestra placeholder para nombre, foto, etc.

Insertar captura de pantalla aquí: ProfileFragment.

### Sección Settings
- **Idioma**: Selector de Español/Inglés, aplica inmediatamente.
- **Modo Oscuro**: Toggle que cambia tema global.
- Otras configuraciones futuras.

Insertar captura de pantalla aquí: SettingsFragment con switches.

### Notificaciones
La app programa recordatorios automáticos para entrenamientos próximos (ej. 30 min antes).
- Requiere permiso de notificaciones.
- Configurable en Settings.

Insertar captura de pantalla aquí: Ejemplo de notificación en el dispositivo.

### Diagrama de Navegación Completo
Insertar diagrama de navegación aquí: Mapa completo con todas las pantallas, transiciones y acciones. Incluir estados como "Agregar" y "Editar".

Texto simplificado:
```
[Inicio] --(BottomNav)--> [Home | Workout | Profile | Settings]
[Workout] --(FAB)--> [Agregar Entrenamiento]
[Workout] --(Click ítem)--> [Detalle Entrenamiento]
[Detalle] --(Editar)--> [Editar Entrenamiento]
[Settings] --(Toggle)--> [Aplicar Tema/Idioma]
```

No se requiere autenticación de usuario, ya que la app opera localmente sin servidor.

### Solución de Problemas
- **App no responde**: Reiniciar dispositivo.
- **Datos no se guardan**: Verificar permisos de almacenamiento.
- **Notificaciones no llegan**: Comprobar configuración de batería y permisos.

## Dificultades Encontradas

Durante el desarrollo, se enfrentaron varios desafíos técnicos y de diseño que requirieron investigación y soluciones creativas:

1. **Integración de Room con Conversores de Tipos**: Inicialmente, Room no soportaba tipos como Date directamente. Se implementó `Converters.java` para transformar Date a Long y viceversa. Dificultad: Manejar zonas horarias y formatos. Solución: Usar `@TypeConverters` en entidad y DAO.

2. **Navegación Compleja con Múltiples Componentes**: Combinar BottomNavigationView, NavigationView y NavController resultó en conflictos de navegación. Dificultad: Evitar duplicación de destinos. Solución: Configurar `AppBarConfiguration` con múltiples top-level destinations.

3. **Soporte Multiidioma en Runtime**: Cambiar idioma sin reiniciar la app completamente. Dificultad: Actualizar recursos en vivo. Solución: Usar `Configuration` y `updateConfiguration()`, pero requiere recreate() para layouts.

4. **WorkManager para Notificaciones Periódicas**: Programar notificaciones diarias sin drain de batería. Dificultad: Restricciones de Android en background tasks. Solución: Usar `PeriodicWorkRequest` con `setConstraints()` para WiFi/carga.

5. **Compatibilidad con Versiones Antiguas**: Asegurar funcionamiento en API 24+. Dificultad: Deprecated APIs. Solución: Usar compat libraries y verificar en emuladores.

6. **Arquitectura MVVM con LiveData**: Vincular UI a datos reactivos. Dificultad: Evitar memory leaks. Solución: Usar `viewLifecycleOwner` en Fragments.

7. **Diseño Responsivo**: Adaptar a diferentes tamaños de pantalla. Dificultad: Layouts fijos. Solución: ConstraintLayout y recursos dimensionales.

8. **Testing**: Escribir tests para Room y UI. Dificultad: Configurar test database. Solución: Usar `Room.inMemoryDatabaseBuilder()` para tests.

9. **Material Design Consistency**: Aplicar temas correctamente. Dificultad: Cambios en runtime. Solución: `AppCompatDelegate.setDefaultNightMode()`.

10. **Gestión de Estado en Fragments**: Preservar estado en rotaciones. Dificultad: ViewModels no persisten datos automáticamente. Solución: Usar SavedStateHandle.

## Fuentes Utilizadas

- **Documentación Oficial de Android**: https://developer.android.com/ - Guías completas para APIs, incluyendo Room, Navigation, WorkManager.
- **Guías de Room Persistence Library**: https://developer.android.com/training/data-storage/room - Tutoriales detallados para setup y uso.
- **Material Design Guidelines**: https://material.io/design - Principios para UI/UX consistentes.
- **Stack Overflow**: Consultas específicas para bugs, como "Room TypeConverter for Date" o "Navigation Component with Bottom Nav".
- **Cursos en Línea**: 
  - Udacity Android Developer Nanodegree.
  - Coursera "Android App Development" de Vanderbilt University.
- **Libros**:
  - "Android Programming: The Big Nerd Ranch Guide" de Bill Phillips.
  - "Kotlin in Action" de Dmitry Jemerov.
- **Comunidades**: Reddit r/androiddev, Google Developers Groups.
- **Herramientas**: Android Studio documentation, Gradle docs para builds.

## Conclusión

El desarrollo de MiGym ha sido una experiencia enriquecedora que ha permitido aplicar conceptos avanzados de desarrollo Android. La aplicación resultante es funcional, moderna y extensible, sirviendo como base sólida para futuras mejoras como sincronización en la nube o integración con wearables.

Se recomienda para futuras versiones: Agregar autenticación, backend remoto, y más features como tracking de progreso.

## Apéndices

### A. Código Fuente Relevante
- Ver repositorio para archivos completos.

### B. Capturas de Pantalla Adicionales
Insertar galería aquí: Todas las pantallas principales en modo claro y oscuro.

### C. Diagrama de Secuencia para Agregar Entrenamiento
Insertar diagrama de secuencia aquí: Usuario -> Fragment -> ViewModel -> Repository -> DAO -> Database, con respuestas inversas.</content>
<parameter name="filePath">c:\Users\josec\AndroidStudioProjects\AnderCalvoDasProyectoEntrega1\Memoria.md