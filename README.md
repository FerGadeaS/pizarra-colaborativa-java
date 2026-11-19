# Pizarra Colaborativa

Aplicación de escritorio desarrollada en Java que permite a múltiples usuarios conectarse a una pizarra compartida en tiempo real. Utiliza una arquitectura cliente-servidor para sincronizar los dibujos y las acciones realizadas por los usuarios conectados.

## Características principales

- Comunicación cliente-servidor mediante Sockets TCP.
- Interfaz gráfica desarrollada con Java Swing.
- Dibujo colaborativo en tiempo real.
- Herramientas para dibujar, borrar y agregar texto.
- Personalización de colores y grosor de los trazos.
- Gestión de múltiples clientes mediante hilos y concurrencia.
- Registro de sesiones de los usuarios en MySQL.
- Sistema de registro e inicio de sesión.

## Tecnologías utilizadas

- **Java:** lenguaje de programación.
- **Java Swing:** desarrollo de interfaces gráficas.
- **Sockets TCP:** comunicación entre cliente y servidor.
- **Multithreading:** gestión de conexiones simultáneas.
- **MySQL:** almacenamiento de usuarios e historial de conexiones.
- **JDBC:** conexión entre Java y MySQL.
- **Apache Ant / NetBeans:** configuración y compilación de los proyectos.

## Estructura del proyecto

```text
ProyectoConcurrente/
├── BaseDeDatos/
│   └── pizarra.sql
├── Cliente/
│   └── src/
├── Servidor/
│   └── src/
├── Documentacion/
│   └── ManualTecnico.pdf
├── lib/
│   └── mysql-connector-j.jar
└── README.md
```

## Requisitos

- Java JDK.
- MySQL Server.
- MySQL Connector/J.
- NetBeans IDE o un entorno compatible con proyectos Java y Apache Ant.

## Configuración

1. Clonar o descargar este repositorio.
2. Crear la base de datos importando el archivo `BaseDeDatos/pizarra.sql` en MySQL.
3. Configurar las credenciales de conexión a MySQL en el proyecto del servidor.
4. Verificar que MySQL Connector/J esté incluido en las dependencias del proyecto.
5. Abrir y compilar los proyectos Cliente y Servidor.

## Ejecución

1. Iniciar el servidor.
2. Ejecutar el cliente.
3. Registrar una cuenta o iniciar sesión.
4. Conectar varios clientes para utilizar la pizarra colaborativa.

**Nota:** El servidor debe estar ejecutándose antes de iniciar las conexiones de los clientes.

## Autor

Proyecto académico desarrollado como