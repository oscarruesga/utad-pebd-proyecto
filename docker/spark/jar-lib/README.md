Se debe copiar en ester directorio los archivos compilados

Desde la raíz del proyecto es necesario empaquetar el proyecto con maven: https://maven.apache.org/index.html [https://maven.apache.org/index.html]

$ mvn package 

Una vez compilado es necesario copiar los archivos en el contenedor

$ cp ./target/meetup-rsvps-spark-es-writer-1.0*.jar ./docker/spark/jar-lib