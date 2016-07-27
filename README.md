# Proyecto Fin de Programa Experto Big Data (UTAD 2016)

Meetup es un web que permite crear eventos públicos y que la gente participe en ellos. Meetup publica una
stream donde se puede seguir en tiempo real la respuestas a las invitaciones a los diferentes grupos. El objetivo de este
proyecto es usar la información provista por Meetup usando tecnologías Big Data con el objetivo de obtener resultados interesantes. 

Origen de los datos: [http://stream.meetup.com/2/rsvps] (http://stream.meetup.com/2/rsvps). La API pública completa está disponible en: [http://www.meetup.com/meetup_api/docs/stream/2/rsvps/#websockets](http://www.meetup.com/meetup_api/docs/stream/2/rsvps/#websockets) . 

El planteamiento del proyecto hace uso de herramientas de BigData como Spark Streaming para el analisis en 'Near Real Time' de los datos que van fluyendo por el stream proporcionado por el servicio.

En el proyecto se incluyen los siguientes entregables:

- [Código Fuente del proyecto](src).
- [Contenedores docker](docker) que dan soporte a la versión distribuida completa de la aplicación.
- [Memoria descriptiva](http://oscar.ruesga.com/memoria_pebd) del proyecto que incluye la descripción del objeto del proyecto, la tecnología utilizada y la arquitectura distribuida desplegada. También se incluye el anális, diseño e implementación de la solución sobre dicha arquitectura.

