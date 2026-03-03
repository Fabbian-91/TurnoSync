# 🚀 TurnoSync  
### Sistema de Gestión de Turnos Cliente/Servidor Concurrente en Java

---

## 📌 Descripción

**TurnoSync** es un sistema de gestión de turnos desarrollado en **Java SE**, bajo una arquitectura **Cliente/Servidor concurrente**, diseñado para simular entornos reales como clínicas, talleres mecánicos o centros de servicio.

El sistema permite que múltiples usuarios soliciten, consulten y cancelen turnos simultáneamente, garantizando integridad, control de concurrencia y seguridad lógica.

El proyecto fue desarrollado para el curso **SC-303 Programación Cliente/Servidor Concurrente**, aplicando:

- Programación Orientada a Objetos (POO)  
- Arquitectura Cliente/Servidor con Sockets TCP  
- Programación Multihilo  
- Patrón Producer–Consumer  
- MVC Extendido  
- JDBC y Base de Datos Relacional  
- Control de roles y autenticación  

---

## 🧠 Arquitectura del Sistema

### 🔷 Visión General

Cliente (Java Swing) ⇄ Servidor Java Concurrente ⇄ Base de Datos Relacional

---

### 🔷 Modelo de Procesamiento

Cliente  
→ ClientHandler (Producer)  
→ RequestQueue (BlockingQueue)  
→ WorkerThread (Consumer)  
→ Capas MVC  
→ Base de Datos  
→ Respuesta al Cliente  

Este diseño desacopla la capa de red de la lógica de negocio y mejora el control de concurrencia.

---

## 🏗 Arquitectura MVC Extendida

El sistema implementa un **MVC extendido**, incorporando:

- Capa de Reglas de Negocio  
- Seguridad Lógica  
- Gestión de Peticiones (Monitoreo)  
- Capa de Red Concurrente  

### 📂 Estructura de Capas

| Capa | Ubicación | Responsabilidad |
|------|-----------|----------------|
| View | `client/view` | Interfaz gráfica (Swing) |
| Controller | `client/controller` y `server/controller` | Coordinación del flujo |
| Service | `server/service` | Reglas de negocio |
| Security | `server/security` | Validaciones y coherencia |
| Network | `server/network` | Sockets y concurrencia |

---

## ⚙️ Concurrencia

El servidor implementa:

- Un hilo receptor por cliente  
- Cola concurrente (`BlockingQueue`)  
- Worker Threads para procesamiento  
- Patrón **Producer–Consumer**  

Esto permite:

- Evitar condiciones de carrera  
- Controlar carga de trabajo  
- Mejorar escalabilidad  
- Separar comunicación y lógica  

---

## 🗄 Modelo de Base de Datos

### Entidades Principales

- Cliente  
- Empleado  
- Usuario  
- Turno  
- Petición  

### Roles del Sistema

- `ADMIN`  
- `CLIENTE`  
- `EMPLEADO`  

Las relaciones están definidas mediante claves foráneas garantizando integridad referencial.

---

## 🔐 Funcionalidades del Sistema

### 👨‍💼 ADMIN

- Crear / editar usuarios  
- Activar / desactivar cuentas  
- Crear turnos disponibles  
- Asignar turnos  
- Cancelar turnos  
- Ver monitoreo de peticiones  
- Consultar historial de operaciones  

### 👤 CLIENTE

- Login / Logout  
- Ver turnos disponibles  
- Solicitar turno  
- Consultar mis turnos  
- Cancelar turno  

### 👨‍🔧 EMPLEADO

- Login  
- Ver turnos asignados  
- Cambiar estado de turno (ej. ATENDIDO)  

---

## 🧩 Tecnologías Utilizadas

- Java SE  
- Java Swing  
- Sockets TCP  
- Multithreading  
- BlockingQueue  
- JDBC  
- MySQL  
- Git y GitHub  

---

## 🛡 Seguridad y Validaciones

El sistema incluye:

- Autenticación con hash de contraseña  
- Control de roles  
- Validación de permisos  
- Prevención de operaciones inválidas  
- Manejo de excepciones  
- Control de coherencia de datos  

---

## 🚨 Manejo de Errores

Se contemplan:

- Errores de conexión  
- Errores de base de datos  
- Fallos de autenticación  
- Operaciones inválidas  
- Errores en procesamiento concurrente  

---

## 🧪 Cómo Ejecutar el Proyecto

### 1️⃣ Configurar Base de Datos

- Crear base de datos en MySQL  
- Ejecutar scripts de creación de tablas  
- Configurar credenciales en el proyecto  

### 2️⃣ Ejecutar Servidor

Ejecutar `ServerMain.java`

### 3️⃣ Ejecutar Cliente

Ejecutar `ClientMain.java`

---

## 📁 Estructura General del Proyecto

```
TurnoSync/
│
├── client/
│   ├── view/
│   ├── controller/
│
├── server/
│   ├── controller/
│   ├── service/
│   ├── security/
│   ├── network/
│
├── database/
├── docs/
└── README.md
```

---

## 🎯 Objetivo del Proyecto

Desarrollar una aplicación cliente/servidor concurrente robusta que demuestre:

- Arquitectura bien estructurada  
- Control real de concurrencia  
- Separación de responsabilidades  
- Seguridad lógica  
- Persistencia confiable  
- Buenas prácticas profesionales  

---

## 📚 Valor Académico y Profesional

TurnoSync integra:

- Arquitectura Cliente/Servidor real  
- Programación Concurrente aplicada  
- Patrón Producer–Consumer  
- MVC Extendido  
- Control de roles y seguridad  
- Monitoreo en tiempo real  

Es un proyecto sólido para:

- Portafolio profesional  
- Entrevistas técnicas  
- Evaluaciones académicas avanzadas  

---

## 👨‍💻 Autor

Anthony Fabian Mora Herrera
Proyecto desarrollado para el curso  
**SC-303 Programación Cliente/Servidor Concurrente**

---

## 📜 Licencia

Proyecto académico con fines educativos.
