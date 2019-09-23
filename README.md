# RappiDemo
Mini proyecto para evaluación técnica para la empresa Rappi.

## Tabla de contenidos

- [Estructura del proyecto](#estructura-del-proyecto)
- [Capas de aplicacion](#capas-de-aplicacion)
- [Contributing](#contributing)
- [Team](#team)
- [FAQ](#faq)
- [Support](#support)
- [License](#license)

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

### Installing

A step by step series of examples that tell you how to get a development env running

Say what the step will be

```
"En este proyecto, decidimos actualizar constantemente el repositorio local, y el viewmodel escucha constantemente cambios
la base de datos local con la ayuda de objetos LiveData, a su vez la vista observa el ViewModel, con esto tenemos
una única fuente de datos."
```

And repeat

```
until finished
```

End with an example of getting some data out of the system or using it for a little demo

## Running the tests

Explain how to run the automated tests for this system

### Break down into end to end tests

Explain what these tests test and why

```
Give an example
```

### And coding style tests

Explain what these tests test and why

```
Give an example
```

## Deployment

Add additional notes about how to deploy this on a live system

## Built With

* [Dropwizard](http://www.dropwizard.io/1.0.2/docs/) - The web framework used
* [Maven](https://maven.apache.org/) - Dependency Management
* [ROME](https://rometools.github.io/rome/) - Used to generate RSS Feeds

## Contributing

Please read [CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) for details on our code of conduct, and the process for submitting pull requests to us.

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/your/project/tags). 

## Authors

* **Billie Thompson** - *Initial work* - [PurpleBooth](https://github.com/PurpleBooth)

See also the list of [contributors](https://github.com/your/project/contributors) who participated in this project.
