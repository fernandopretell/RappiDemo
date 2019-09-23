# RappiDemo
Mini proyecto para evaluación técnica para la empresa Rappi.

## Tabla de contenidos

- [Estructura del proyecto](#estructura-del-proyecto)
- [Capas de aplicacion](#capas-de-aplicacion)
- [Responsabilidad de clases](#responsabilidad-de-clases)
- [Responsabilidad única](#responsabilidad-unica)
- [Código limpio](#codigo-limpio)

---
## Estructura del proyecto
El proyecto tiene dos módulos: App y componentes:



### Estructura módulo app:
Módulo principal donde se encuentras las actividades y archivos pertenecientes a la arquitectura de componentes MVVM.

- __rappidemo__
  - __base__
    - [BaseActivity.kt](app/src/main/java/com/fernandopretell/rappidemo/base/BaseActivity.kt)
  - __model__
    - [CardModelParcelable.kt](app/src/main/java/com/fernandopretell/rappidemo/model/CardModelParcelable.kt)
    - [Pelicula.kt](app/src/main/java/com/fernandopretell/rappidemo/model/Pelicula.kt)
    - [ResponseFinal.kt](app/src/main/java/com/fernandopretell/rappidemo/model/ResponseFinal.kt)
  - __presentation__
    - [BienvenidaActivity.kt](app/src/main/java/com/fernandopretell/rappidemo/presentation/BienvenidaActivity.kt)
    - [BuscadorActivity.kt](app/src/main/java/com/fernandopretell/rappidemo/presentation/BuscadorActivity.kt)
    - [MainActivity.kt](app/src/main/java/com/fernandopretell/rappidemo/presentation/MainActivity.kt)
    - [VistaDetalleActivity.kt](app/src/main/java/com/fernandopretell/rappidemo/presentation/VistaDetalleActivity.kt)
  - __repository__
    - [PeliculasRepository.kt](app/src/main/java/com/fernandopretell/rappidemo/repository/PeliculasRepository.kt)
  - __source__
    - __local__
      - [PeliculasDatabase.kt](app/src/main/java/com/fernandopretell/rappidemo/source/local/PeliculasDatabase.kt)
      - [ResponseEntity.kt](app/src/main/java/com/fernandopretell/rappidemo/source/local/ResponseEntity.kt)
      - [ResponseDao.kt](app/src/main/java/com/fernandopretell/rappidemo/source/local/ResponseDao.kt)
      - [ResultConverter.kt](app/src/main/java/com/fernandopretell/rappidemo/source/local/ResultConverter.kt)
    - __remote__
      - [HelperWs.kt](app/src/main/java/com/fernandopretell/rappidemo/source/remote/HelperWs.kt)
      - [ResponseApi.kt](app/src/main/java/com/fernandopretell/rappidemo/source/remote/ResponseApi.kt)
      - [WebServiceData.kt](app/src/main/java/com/fernandopretell/rappidemo/source/remote/WebServiceData.kt)
      - [WebServiceImages.kt](app/src/main/java/com/fernandopretell/rappidemo/source/remote/WebServiceImages.kt)
  - __util__
    - [ConnectivityReceiver.kt](app/src/main/java/com/fernandopretell/rappidemo/util/ConnectivityReceiver.kt)
    - [Constants.kt](app/src/main/java/com/fernandopretell/rappidemo/util/Constants.kt)
    - [DownloadFileAsyncTask.java](app/src/main/java/com/fernandopretell/rappidemo/util/DownloadFileAsyncTask.java)
  - __viewmodel__
    - [PeliculaViewModel.kt](app/src/main/java/com/fernandopretell/rappidemo/viewmodel/PeliculaViewModel.kt)
    

### Estructura módulo Componentes:
En este módulo se encuentran las vistas personalizadas(Custom View).
- __componentes__
  - __button__
    - [Button.kt](componentes/src/main/java/com/fernandopretell/componentes/button/Button.kt)    
  - [Constants_comp.kt](componentes/src/main/java/com/fernandopretell/componentes/Constants_comp.kt)
  - __carousel__
    - [Banner.kt](componentes/src/main/java/com/fernandopretell/componentes/carousel/Banner.kt)
    - [Card_pelicula.kt](componentes/src/main/java/com/fernandopretell/componentes/carousel/Card_pelicula.kt)
    - [Carousel.kt](componentes/src/main/java/com/fernandopretell/componentes/carousel/Carousel.kt)
    - [CarouselAdapter.kt](componentes/src/main/java/com/fernandopretell/componentes/carousel/CarouselAdapter.kt)
    - __models__
      - [BannerModel.kt](componentes/src/main/java/com/fernandopretell/componentes/carousel/models/BannerModel.kt)
      - [CardModel.kt](componentes/carousel/models/CardModel.kt)
  - __list_bienvenida__
    - [BienvenidaAdapter.kt](componentes/src/main/java/com/fernandopretell/componentes/list_bienvenida/BienvenidaAdapter.kt)
    - [Lista_bienvenida.kt](componentes/src/main/java/com/fernandopretell/componentes/list_bienvenida/Lista_bienvenida.kt)
    - __models__
      - [ItemBienvenida.kt](componentes/src/main/java/com/fernandopretell/componentes/list_bienvenida/models/ItemBienvenida.kt)             

---
## Capas de aplicación
 Para el proyecto utilizamos la arquitectura de componentes, recomendada por Google. Adicionalmente usamos objetos LiveData,
 que son obejtos reactivos, tienen el beneficio de restepar el ciclo de vida de nuestros componentes, 
 solo invoca a su devolución de llamada solo cuando la vista este activa.
- **Model**
  --> En esta capa se encuentra la lógica de negocio y las conexiones a los datos remotos y locales, 
  a esta capa pertenecen los paquetes `repository` y `source`.
- **View**
  --> En esta capa se encuentran la interfaz de usurario, esta capa se encuentra observando al View Model para actualizarse cuando los datos cambien.
- **ViewModel**
  --> Esta capa se encarga de preparar y administrar los datos a la interfaz de usuario, y es observado por esta. Contiene objetos LiveData para captar cambios en la base de datos local, y transmitirlos a la capa View. También sobrevive a los cambios de configuracion de la activity y/o fragment que la observa porque tiene un ciclo de vida independiente.
  
  
  <a href="http://fvcproductions.com"><img src="https://miro.medium.com/max/3840/1*6YYuni9J8nDNjMAYh1TIAQ.jpeg" title="FVCproductions" alt="FVCproductions"></a>

```
"En este proyecto, decidimos actualizar constantemente el repositorio local, el ViewModel
escucha constantemente cambios la base de datos local con la ayuda de objetos LiveData,
a su vez la Vista observa el ViewModel, con esto tenemos una única fuente de datos."
```
---
## Responsabilidad de clases

- BaseActivity.kt --> Es la clase base de las actividades, la cual contiene los receptores del cambio de status de red, y       configuración de animaciones de entrada y salida de la actividades.
- CardModelParcelable.kt --> Versión parcelable de objeto CardModel, que nos permite ser adjuntado en el intent para abrir el   detalle de cada película.
- Pelicula.kt --> Objeto parte de la respuesta a la solicitud http, es a su vez parte del objeto ResponseApi.kt.
- ResponseFinal.kt --> Objeto producto de la transformacion de un ResponseAPi o un ResponseEntity.
- BienvenidaActivity.kt --> Actividad de bienvenida, en la cual podemos elegir que libreria de peliculas queremos ver.
- BuscadorActivity-kt --> Buscador de películas, sensible a cada caracter escrito.
- MainActivity.kt --> Actividad principal en la cual podemos ver todo el repertorio de películas disponibles.
- VistaDetalleActivity --> Actividad donde poemos ver el detalle de cada película.
- PeliculasRepository.kt --> Esta clase es nuestro repositorio de datos, se encarga de la lógica de recolección de datos.
- PeliculasDatabase.kt --> Instancia de base de datos local (Room).
- ResponseEntity.kt --> Instancia de la tabla donde guardaremos los datos.
- ResponseDao.kt --> Interfaz con metodos para interactuar con la base de datos.
- ResultConverter.kt --> Esta clase la necesitamos para poder insertar una lista de peliculas en la base de datos local, en     tiempo de ejecucion se usan los métodos aquí presentes.
- HelperWs.kt --> En esta clase se configura la interacción con la API "The movie database"
- ResponseApi.kt --> Objeto respuesta de la API
- WebServiceData.kt --> Interfaz con el metodo para captar la data de la API, esta clase es parte de retrofit.
- WebServiceImages.kt --> Interfaz con el metodo para obtener las imagenes de la API y guardalas en Room.
- ConnectivityReceiver.kt --> Broadcast receiver que nos ayuda a captar los eventos status de conexion a datos.
- Constants.kt --> Contantes.
- DownloadFileAsyncTask.java --> Con esta clase descargo las imagenes, usando AsyncATask.
- PeliculaViewModel.kt --> Clase ViewModel, se encarga de preparar y administrar los datos para la vista.
- MÓDULO COMPONENTES --> Aqui estan todas las clases encargadas de construir las custom views.

---
## Responsabilidad única
Este concepto busca que cada clase sea responsable de una sola cosa, es parte de los cinco principios S-O-L-I-D.
Tiene como proposito desacoplar el código lo mas que se pueda, esto permite que la aplicación sea mas escalable y testeable.
Tambien podemos mencionar

---
## Código limpio 
Existen varios frentes de los cuales se atacar el concepto de "Buen código o código limpio":
- En el día a día, un código lo considero limpio, cuando expresa claramente su trabajo, ya que el código también necesita ser ententido por todo el equipo de desarrollo.
- La estructura del código es también algo importante, que cada clase esté donde debe estar, según la arquitectura en uso.
- Por otro lado la pruebas unitarios son otro factort importante, ya que estos garantizan el funcionamiento de nuestro código, quizas lleva más tiempo construir pruebas unitarias en paralelo a nuestro desarrollo, pero a la larga este codigo test, va a permitir encontrar errores en nuestro codigo rapidamente.
- Yendo a un paso mas macro, lo primero a tomar en cuenta es los principios S-O-L-I-D.
-Otra buena manera de ver un buen proyecto es ver la dimensión del proyecto, para proyectos grandes ya creados con Clean Architecture, con View y Presenters en la capa de ´Presentacion´, casos de uso y logica de negocio en la capa `Domain`, y acceso a datos en la capa `Data`, recomendaria seguir adelante con ellos, teniendo siempre presente que debemos usar en lo posible custom views que nos quitan el trabajo de renderizado al sistema operativo.
Por otro lado si es un proyecto ligero que no tenga mucho  tráfico de datos, y no esta creado, recomedaria MVVM, ya que es lo recomendado por Google, y permite usar los nuevos componentes de Android JetPack.
Otra casuística seria empezar un proyecto grande desde cero, en ese caso usaria un MVVM siendo parte de un Clean Architecture, sustiyendo los presenters por ViewModels, y usando las coruotines para llamadas asincronas, entre los otros componentes de Android Jetpack (Paging, WorkManager, LiveData, Navigation, etc).
-Tambien es una buena práctica, usar inyeccion de dependencias, que nos quitan la posibilidad de instanciar objetos en clases, mas bien nos los inyectan cuando sean necesarios. Con esta herramienta tambien estamos acatando uno de los principios SOLID (D).






