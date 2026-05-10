# Programa de Gestión de Usuarios con interfaz gráfica hecha en SWING y conexión a MySQL con CRUD

**Autor:** Víctor Touriño  
**Curso:** 1 DAM
**Fecha:** 10/05/2026
**Ejercicio:** Gestión de Usuarios

## Descripción del Proyecto
Esta aplicación es un sistema completo de Gestión de Usuarios desarrollado en Java con interfaz gráfica hecha con Swing y conexión a base de datos MySQL. Permite realizar operaciones CRUD (Create, Read, Update, Delete) de forma intuitiva a través de botones en la ventana principal.

Mediante PreparedStatement protejo la base de datos contra ataques de inyección SQL a la hora de insertar los datos, como bien se ha explicado en clase.

He implementado JBCrypt para hashear las contraseñas de los usuarios y así aumentar la seguridad del programa. En la tabla, en la columna "Contraseña" se muestra la contraseña hasheada a propósito.

Gracias a JavaDoc se ha generado una carpeta "documentacion" en la cual abriendo el "index.html" se puede acceder a una explicación de toda la documentación en formato web.

## Tecnologías Utilizadas
* **Lenguaje:** Java 
* **Interfaz Gráfica:** Java Swing
* **Base de Datos:** MySQL
* **Conectividad:** JDBC
* **Seguridad:** JBCrypt