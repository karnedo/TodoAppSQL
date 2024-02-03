# To-do App

# Modelo-Vista-Controlador
La aplicación está construída en base a la arquitectura Modelo-Vista-Controlador, utilizando los controladores
para la conexión a la base de datos

# Características
Las características de la aplicación son idénticas a las de su proyecto precedesor con Volley, solo
que este hace uso del conector JDBC:

## CRUD

La aplicación realiza un CRUD para las tareas, además de presentar la habilidad de subir y descargar
imágenes, así como una pantalla de registro e inicio de sesión.

## Base de datos

El fichero SQL con la estructura de la base de datos se encuentra en el directorio raíz bajo el nombre
de "database". Este está preparado para ser importando directamente desde la página de PhpMyAdmin. En adición, también se encuentran los ficheros PHP.

El resto de características son idénticas a su proyecto homólogo de Firebase.

## Corrección

Para la corrección de la aplicación, asegúrese de que cambia el valor de la variable HOST
de la clase DBConnection a la IP de su servidor. Si no tiene el puerto por defecto que utiliza MySQL, cambie la variable PORT.
Asimismo, cambie el usuario y la contraseña al usuario donde tenga la base de datos. La base de datos es la misma que la
del proyecto anterior. Igualmente se ha proporcionado en el directorio raíz del proyecto
un fichero SQL con el que podrá importar esa misma base de datos.